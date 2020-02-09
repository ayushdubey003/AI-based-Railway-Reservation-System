from flask import Flask, jsonify
import requests 
from bs4 import BeautifulSoup
import csv
import re
import joblib # install 
import lightgbm as lgm # install
import numpy as np # install
from sklearn.preprocessing import StandardScaler # install
from datetime import datetime
import os
import sys

app = Flask(__name__)

# with open('./stations.csv', 'rt') as file:
#     reader = csv.reader(file)
#     # for row in reader:
#     #     print(row)
app.run(host="0.0.0.0", port=5000, debug=True)

@app.route("/")
def home():
    return 'hii...there 3!!!'

@app.route("/alternates/<board>/<destination>/<intermediates>/<doj>", methods=['GET'])
def alternates(board,destination,intermediates,doj):
    cmd = "./Algorithm "+board+" "+destination+" "+intermediates+" "+doj
    os.system(cmd)
    file=""
    with open("result.txt","r") as readFile:
        file = readFile.read()
    u = file.split('\n')
    i = 0
    alternates = []
    while True:
        if i >= len(u) - 1:
            break
        stations = []
        trains = []
        time =""
        stations = u[i].split(" ")
        trains = u[i+1].split(" ")
        time = u[i+2]
        alternates.append({
            "stations":stations,
            "trains" :trains,
            "time" :time
            })
        i = i+3
    return jsonify(alternates=alternates)

def predict_probability(train_days, train_type, booking_date, booking_hour, journey_date, journey_hour, ticket_class, waiting_list_category, waiting_list_number):
    train_metric_type = [0, 0, 0, 0]
    if train_type > 0:
        train_metric_type[train_type - 1] = 1
    
    date_item_booking = list(map(int, booking_date.split('-')))
    date_item_journey = list(map(int, journey_date.split('-')))
    
    booking_year = date_item_booking[0]
    journey_year = date_item_journey[0]
    
    booking_month = date_item_booking[1]
    journey_month = date_item_journey[1]
    
    booking_day = date_item_booking[2]
    journey_day = date_item_journey[2]
    
    booking_datetime = datetime(booking_year, booking_month, booking_day, hour=booking_hour)
    journey_datetime = datetime(journey_year, journey_month, journey_day, hour=journey_hour)
    
    time_difference_1 = (journey_datetime - booking_datetime).total_seconds() // 3600
    assert time_difference_1 >= 0
    time_difference_2 = 0
    
    journey_month_type = [0] * 11
    if journey_month > 1:
        journey_month_type[journey_month - 2] = 1
    
    ticket_class_type = [0, 0, 0]
    if ticket_class == 'SL':
        ticket_class_type[2] = 1
    if ticket_class == '2A':
        ticket_class_type[0] = 1
    if ticket_class == '3A':
        ticket_class_type[1] = 1
    
    waiting_list_type = [0, 0, 0, 0]
    if waiting_list_category == 'RL':
        waiting_list_type[2] = 1
    if waiting_list_category == 'TQ':
        waiting_list_type[3] = 1
    if waiting_list_category == 'PQ':
        waiting_list_type[0] = 1
    if waiting_list_category == 'RA':
        waiting_list_type[1] = 1

    row = []
    row.append(train_days)
    row.append(time_difference_1)
    row.append(0)
    row.append(waiting_list_number)
    row.extend(ticket_class_type)
    row.extend(waiting_list_type)
    row.extend(train_metric_type)
   
    print(train_days, train_type, time_difference_1, time_difference_2, waiting_list_number, journey_month)
    X = np.array([row])
    
    model = joblib.load('../datasets/lgbtqmodel.pkl')
    scalar = joblib.load('../datasets/scaler_file.pkl')
    X_sc = scalar.transform(X)
    
    return model.predict(X_sc)[0]
    
@app.route("/predict/<train_number>/<booking_date>/<booking_time>/<journey_date>/<journey_time>/<ticket_class>/<waiting_list>",methods=['GET'])
def predict(train_number, booking_date, booking_time, journey_date, journey_time, ticket_class, waiting_list):
    try:
        train_dict = joblib.load('../datasets/train_dictionary.pkl')
        train_number = int(str(train_number).strip())
        assert train_number in train_dict.keys()
        train_days = train_dict[train_number]['days']
        train_type = train_dict[train_number]['type']

        date_pattern = re.compile('[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]')
        assert len(date_pattern.findall(booking_date)[0]) == len(booking_date)
        assert len(date_pattern.findall(journey_date)[0]) == len(journey_date)

        time_pattern = re.compile('[0-9][0-9]:[0-9][0-9]')
        assert len(time_pattern.findall(booking_time)[0]) == len(booking_time)
        assert len(time_pattern.findall(journey_time)[0]) == len(journey_time)
        booking_hour = int(booking_time[0:2])
        journey_hour = int(journey_time[0:2])
        
        print(waiting_list)
        assert ticket_class in ['1A', '2A', '3A', 'SL']
        waiting_list_type = waiting_list[0:2]
        assert waiting_list_type in ['GN', 'RL', 'TQ', 'RA', 'PQ']
        for i in range(-1, -len(waiting_list) - 1, -1):
            if not (waiting_list[i] in [str(j) for j in range(10)]):
                break
        waiting_number = int(waiting_list[i + 1:])
        print(waiting_number)
        
    except Exception as e:
        print(e)
        return e.__str__()

    return jsonify(prediction=predict_probability(train_days, train_type, booking_date, booking_hour, journey_date, journey_hour, ticket_class, waiting_list_type, waiting_number)*100)


@app.route("/list/version", methods=['GET'])
def stationslistversion():
    ans={
        "version":"1.0.1"
    }
    return ans

@app.route("/level/<code>")
def level(code):
    return jsonify(name=code)

@app.route("/trains", methods=['GET'])
def listofalltrains():
    ans = []
    i = 0
    with open("../datasets/combinedtrains.csv","rt") as readFile:
        file = csv.reader(readFile)
        for row in file:
            i = i+1
            if i==1:
                continue
            ans.append({
                "trainNo": row[0],
                "trainName" : row[1]
                });
    return jsonify(trains=ans)

@app.route("/trains/<board>", methods=['GET'])
def listoftrainssourceindependent(board):
    ans=[]
    if(True):
        with open("../datasets/trains.csv",'rt') as file:
            reader = csv.reader(file)
            for row in reader:
                flag = 0
                med_stations = row[7]
                sep_stations = med_stations.split('$')
                for med in sep_stations:
                    if med == board:
                        classes = row[3].split("\u00a0")
                        arr = row[8].split("$")
                        dept = row[9].split("$")
                        namedRoutes = row[6].split("$")
                        codedRoutes = row[7].split("$")
                        days = row[2].split(" ")
                        rt = row[10].split("$")
                        sld = row[11].split("$")
                        sd = row[12].split("$")
                        can = row[13].split("$")

                        ans.append({
                                "train no": row[0],
                                "train name": row[1],
                                "running days": days,
                                "available classes": classes,
                                "type": row[4],
                                "zone": row[5],
                                "route with names": namedRoutes,
                                "route with codes": codedRoutes,
                                "arrival times": arr,
                                "departure times": dept,
                                "right time" : rt,
                                "slight delay" : sld,
                                "significant delay" : sd,
                                "cancelled/unknown" : can
                            })
                        break

    return jsonify(trains=ans)

@app.route("/trains/<board>/<destination>", methods=['GET'])
def listoftrains(board, destination):
    ans = []
    if(True):
        with open('../datasets/trains.csv', 'rt') as file:
            reader = csv.reader(file)
            for row in reader:
                flag = 0
                med_stations = row[7]
                sep_stations = med_stations.split('$')
                for med in sep_stations:
                    if med == board:
                        if flag == 0:
                            flag += 1
                    if med == destination:
                        if flag == 1:
                            flag += 1
                    if flag == 2:
                        classes = row[3].split("\u00a0")
                        arr = row[8].split("$")
                        dept = row[9].split("$")
                        namedRoutes = row[6].split("$")
                        codedRoutes = row[7].split("$")
                        days = row[2].split(" ")
                        rt = row[10].split("$")
                        sld = row[11].split("$")
                        sd = row[12].split("$")
                        can = row[13].split("$")

                        ans.append({
                                "train no": row[0],
                                "train name": row[1],
                                "running days": days,
                                "available classes": classes,
                                "type": row[4],
                                "zone": row[5],
                                "route with names": namedRoutes,
                                "route with codes": codedRoutes,
                                "arrival times": arr,
                                "departure times": dept,
                                "right time" : rt,
                                "slight delay" : sld,
                                "significant delay" : sd,
                                "cancelled/unknown" : can
                            })
                        break

    return jsonify(trains=ans)

@app.route("/trains/<board>/<destination>/<doj>/<travelclass>", methods=['GET'])
def listoftrainsdaywise(board, destination,doj,travelclass):
    ans = []

    if(True):
        with open('../datasets/trains.csv', 'rt') as file:
            reader = csv.reader(file)
            for row in reader:
                flag = 0
                med_stations = row[7]
                sep_stations = med_stations.split('$')
                for med in sep_stations:
                    if med == board:
                        if flag == 0:
                            flag += 1
                    if med == destination:
                        if flag == 1:
                            flag += 1
                    if flag == 2:
                        classes = row[3].split("\u00a0")
                        arr = row[8].split("$")
                        dept = row[9].split("$")
                        namedRoutes = row[6].split("$")
                        codedRoutes = row[7].split("$")
                        days = row[2].split(" ")
                        rt = row[10].split("$")
                        sld = row[11].split("$")
                        sd = row[12].split("$")
                        can = row[13].split("$")
                        z= True
                        # for day in days:
                        #     if day.lower() == doj.lower():
                        #         z=True
                        #         break

                        k = False

                        for tc in classes:
                            if tc.lower() == travelclass.lower():
                                k = True
                                break

                        pos = 0
                        for i in range(0,len(codedRoutes)):
                            if codedRoutes[i] == board:
                                pos = i
                                break

                        u = int(dept[pos].split("(")[1].split(")")[0].split(" ")[1])
                        pos1=0
                        temp = ['MON','TUE','WED','THU','FRI','SAT','SUN']

                        for i in range(0,len(temp)):
                            if temp[i].lower() == doj.lower():
                                pos1 = i
                                break
                        diff = pos1 - u + 1
                        if diff < 0:
                            diff = diff + 7

                        v = False
                        if days[0].lower()!="daily":
                            for i in range(0,len(days)):
                                if days[i].lower()==temp[diff].lower():
                                    v = True
                                    break
                        else:
                            v = True

                        if(((len(days) == 1 and days[0].lower() == "daily") or z) and k and v):
                            ans.append({
                                    "train no": row[0],
                                    "train name": row[1],
                                    "running days": days,
                                    "available classes": classes,
                                    "type": row[4],
                                    "zone": row[5],
                                    "route with names": namedRoutes,
                                    "route with codes": codedRoutes,
                                    "arrival times": arr,
                                    "departure times": dept,
                                    "right time" : rt,
                                    "slight delay" : sld,
                                    "significant delay" : sd,
                                    "cancelled/unknown" : can
                                })
                        break

    return jsonify(trains=ans)

@app.route("/seats/<trainno>/<board>/<destination>/<travelClass>/<doj>/<quota>", methods=['GET'])
def seat_availability(trainno, board, destination, travelClass, doj, quota):
    url = "https://www.railyatri.in/seat-availability/" +trainno+"-"+board+"-"+destination+"?journey_class="+travelClass+"&journey_date="+doj+"&quota="+quota
    try:
        content = requests.get(url)
        soup = BeautifulSoup(content.text, 'html.parser')

        try:
            availability = []
            x = soup.find('div',{"id":"one"}).find_all('div',{"class":"seat_availability_details"})
            for i in range(0,len(x)):
                v = "first"+str(i+1)
                availability.append(x[i].find('span',{"class":"status_one"}).find('div',{"id":v}).text)
            return jsonify(seatavailability=availability)
        except Exception as e:
            return jsonify(error = str(e))

    except Exception as e:
        return jsonify(error = str(e))

@app.route("/list", methods=['GET'])
def listofstations():
    flag = 0

    ans = []
    with open('../datasets/stations.csv', 'rt') as file:
        reader = csv.reader(file)
        for row in reader:
            if flag == 0:
                flag = 1
                continue
            else:
                coordinates =[]
                coordinates.append(row[2])
                coordinates.append(row[3])
                ans.append({
                    'name': row[0],
                    'code': row[1],
                    "coordinates": coordinates
                })

    return jsonify(stations=ans)

@app.route("/pnrstatus/<pnr>", methods=['GET'])
def pnrstatus(pnr):
    url = "https://www.railyatri.in/pnr-status/" +pnr
    try:
        content = requests.get(url)
        soup = BeautifulSoup(content.text, 'html.parser')

        try:
            entire_pnr_info = soup.find('div',{"class":"pnr-search-result-blk"}).find('div',{"class":"pnr-search-result-info"}).find('div',{"class":"row"})
            train_info = entire_pnr_info.find('div',{"class":"train-info"}).find('p',{"class":"pnr-bold-txt"}).text
            route_info = entire_pnr_info.find('div',{"class":"train-route"}).find_all('div',{"class":"col-xs-4"})
            board_info = entire_pnr_info.find('div',{"class":"boarding-detls"}).find_all('div',{"class":"col-xs-4"})
            status = soup.find('div',{"id":"status"}).find_all('div',{"class":"chart-stats"})

            info = train_info.split("-")
            trainNo = info[0].strip()
            trainName = ""
            for i in range(1,len(info)):
                trainName = trainName + " "+ info[i].strip()

            src = route_info[0].find('p',{"class":"pnr-bold-txt"}).text.strip().split("|")
            srcName = src[0].strip()
            srcCode = src[1].strip()
            dest = route_info[1].find('p',{"class":"pnr-bold-txt"}).text.strip().split("|")
            destName = dest[0].strip()
            destCode = dest[1].strip()
            srctime = route_info[0].find_all('p')[2].text.strip()
            desttime = route_info[1].find_all('p')[2].text.strip()
            dura = route_info[2].find_all('span',{"class":"pnr-bold-txt"})[0].text.strip()+":"+route_info[2].find_all('span',{"class":"pnr-bold-txt"})[1].text.strip()

            day = board_info[0].find('p',{"class":"pnr-bold-txt"}).text.strip()
            tc = board_info[1].find('p',{"class":"pnr-bold-txt"}).text.strip()
            pf = board_info[2].find('p',{"class":"pnr-bold-txt"}).text.strip()

            bs = []
            cs = []

            for i in range(1,len(status)):
                stat = status[i].find_all('div',{"class":"col-xs-4"})
                bs.append(stat[0].find('p',{"class":"pnr-bold-txt"}).text.strip())
                cs.append(stat[1].find('p',{"class":"pnr-bold-txt"}).text.strip())

            ans={
                'pnr':pnr,
                'trainNo':trainNo,
                'trainName':trainName.strip(),
                'fromName':srcName,
                'fromCode':srcCode,
                'fromTime':srctime,
                'toName':destName,
                'toCode':destCode,
                'toTime':desttime,
                'duration':dura,
                'doj':day,
                'class':tc,
                'platform':pf,
                'bookingStatus':bs,
                'currentStatus':cs
            }
            return jsonify(status=ans)
        except Exception as e:
            return jsonify(error = str(e))

    except Exception as e:
        return jsonify(error = str(e))

@app.route("/fareenquiry/<trainno>/<src>/<destination>", methods=['GET'])
def fareenquiry(trainno,src,destination):
    url = "https://erail.in/train-fare/"+trainno+"?from="+src+"&to="+destination
    try:
        content = requests.get(url)
        soup = BeautifulSoup(content.text,'html.parser')
        try:
            tr = soup.find_all('div',{"class":"panel"})[1].find('table',{"class":"tableSingleFare"}).find_all('tr')
            classes = []

            for i in range(1,len(tr[0].find_all('th'))):
                classes.append(tr[0].find_all('th')[i].text.strip())

            adult=[]
            child=[]
            adultTatkal=[]
            childTatkal=[]
            senFemale=[]
            senMale=[]

            for i in range(1,len(tr)):
                td = tr[i].find_all('td')
                if i==1:
                    for j in range(1,len(td)):
                        try:
                            adult.append({
                                classes[j-1]:"₹ "+td[j].find('b').text.strip()
                            })
                        except Exception as ee:
                            adult.append({
                                classes[j-1]:"₹ "+td[j].text.strip()
                            })
                if i==2:
                    for j in range(1,len(td)):
                        try:
                            child.append({
                                classes[j-1]:"₹ "+td[j].find('b').text.strip()
                            })
                        except Exception as ee:
                            child.append({
                                classes[j-1]:"₹ "+td[j].text.strip()
                            })
                if i==3:
                    for j in range(1,len(td)):
                        try:
                            adultTatkal.append({
                                classes[j-1]:"₹ "+td[j].find('b').text.strip()
                            })
                        except Exception as ee:
                            adultTatkal.append({
                                classes[j-1]:"₹ "+td[j].text.strip()
                            })
                if i==4:
                    for j in range(1,len(td)):
                        try:
                            childTatkal.append({
                                classes[j-1]:"₹ "+td[j].find('b').text.strip()
                            })
                        except Exception as ee:
                            childTatkal.append({
                                classes[j-1]:"₹ "+td[j].text.strip()
                            })
                if i==5:
                    for j in range(1,len(td)):
                        try:
                            senFemale.append({
                                classes[j-1]:"₹ "+td[j].find('b').text.strip()
                            })
                        except Exception as ee:
                            senFemale.append({
                                classes[j-1]:"₹ "+td[j].text.strip()
                            })
                if i==6:
                    for j in range(1,len(td)):
                        try:
                            senMale.append({
                                classes[j-1]:"₹ "+td[j].find('b').text.strip()
                            })
                        except Exception as ee:
                            senMale.append({
                                classes[j-1]:"₹ "+td[j].text.strip()
                            })

            fare = {
                "adult":adult,
                "child":child,
                "adultTatkal":adultTatkal,
                "childTatkal":childTatkal,
                "seniorFemale":senFemale,
                "seniorMale":senMale
            }

            return jsonify(fare=fare)
        except Exception as ex:
            return jsonify(error=str(ex))
    except Exception as e:
        return jsonify(error=str(e))

@app.route("/livestation/<src>/<destination>/<hours>", methods=['GET'])
def livestation(src,destination,hours):
    url = ""
    if destination == "null":
        url = "https://www.confirmtkt.com/train-LiveStation.php?sourcestation="+src.strip()+"&destinationstation=&hours="+hours
    else:
        url = "https://www.confirmtkt.com/train-LiveStation.php?sourcestation="+src.strip()+"&destinationstation="+destination.strip()+"&hours="+hours      
    ans = []
    try:
        content = requests.get(url)
        soup = BeautifulSoup(content.text,'html.parser')
        u = soup.find('table',{"id":"trainDetails1"}).find_all("tbody")
        for i in range(0,len(u)):
            v = u[i].find("tr").find_all("td")
            x = {
                "trainNo" : v[0].text.strip(),
                "trainName" : v[1].text.strip(),
                "schTimeArrival" : v[2].text.strip(),
                "schTimeDeparture" : v[3].text.strip(),
                "expTimeArrival" : v[4].text.strip(),
                "expTimeDeparture" : v[5].text.strip(),
                "delay" : v[6].text.strip(),
                "platformNo" : v[7].text.strip()
            }
            ans.append(x)
    except Exception as e:
        return (jsonify(error = str(e)))
    return jsonify(livestation = ans)

@app.route("/status/<trainno>/<doj>", methods=['GET'])
def livestatus(trainno,doj):
    ans =[]
    url = "https://railenquiry.in/runningstatus/"+trainno+"/"+doj
    try:
        content = requests.get(url)
        soup = BeautifulSoup(content.text,'html.parser')
        u = soup.find('table',{"class":"table-responsive"}).find("tbody").find_all("tr")
        for i in range(0,len(u)-1):
            x = u[i].find_all("td")
            ans.append({
                "station" : x[1].text,
                "schArr" : x[3].text,
                "expArr" : x[4].text,
                "arrDelay" : x[5].text,
                "schDept" : x[6].text,
                "expDept" : x[7].text,
                "delayDept" : x[8].text
                })
    except Exception as e:
        return (jsonify(error = str(e)))
    return jsonify(status = ans)


if __name__ == '__main__':
    app.run()
list

from flask import Flask, jsonify
import requests 
from bs4 import BeautifulSoup
import csv
app = Flask(__name__)

# with open('./stations.csv', 'rt') as file:
#     reader = csv.reader(file)
#     # for row in reader:
#     #     print(row)
app.run(host="0.0.0.0", port=5000, debug=True)

@app.route("/")
def home():
    return 'hii...there 3!!!'

@app.route("/list/version", methods=['GET'])
def stationslistversion():
    ans={
        "version":"1.0.0"
    }
    return ans

@app.route("/level/<code>")
def level(code):
    return jsonify(name=code)

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

if __name__ == '__main__':
    app.run()
list
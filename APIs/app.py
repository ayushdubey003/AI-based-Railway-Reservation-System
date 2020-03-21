from flask import Flask, jsonify, request
from flask_cors import CORS, cross_origin
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
import json
from hashlib import sha256
import time

app = Flask(__name__)

cors = CORS(app)
app.config['CORS_HEADERS'] = 'Content-Type'

# with open('./stations.csv', 'rt') as file:
#     reader = csv.reader(file)
#     # for row in reader:
#     #     print(row)
# app.run(host="0.0.0.0", port=5000, debug=True)

@app.route("/")
@cross_origin()
def home():
    return 'hii...there 3!!!'

@app.route("/alternates/<board>/<destination>/<intermediates>/<doj>/<mintime>/<maxtime>", methods=['GET'])
def alternates(board,destination,intermediates,doj,mintime,maxtime):
    cmd = "./Algorithm "+board+" "+destination+" "+intermediates+" "+doj+" "+mintime+" "+maxtime+" 1"
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
        departureTime = u[i+3].split(" ")
        stations = stations[:-1]
        trains = trains[:-1]
        departureTime = departureTime[:-1]
        alternates.append({
            "stations":stations,
            "trains" :trains,
            "time" :time,
            "departureTime" : departureTime
            })
        i += 4
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
    print(url)
    try:
        content = requests.get(url)
        soup = BeautifulSoup(content.text, 'html.parser')

        try:
            availability = []
            x = soup.find_all('div',{"class":"sa-quota-block"})[0].find_all('div',{"class":"availability-block"})[0].find_all('div',{"class":"row"})
            for i in range(0,len(x)):
                v = "first"+str(i+1)
                availability.append(x[i].find_all('div',{"class":"col-md-4"})[0])
            print(availability)
            return jsonify(seatavailability=availability)
        except Exception as e:
            print(e)
            return jsonify(error = str(e))

    except Exception as e:
        return jsonify(error = str(e))

@app.route("/list", methods=['GET'])
def listofstations():
    flag = 0

    ans = []
    with open('../datasets/finalpincodes.csv', 'rt') as file:
        reader = csv.reader(file)
        for row in reader:
            if flag == 0:
                flag = 1
                continue
            else:
                coordinates =[]
                coordinates.append(row[3])
                coordinates.append(row[4])
                ans.append({
                    'name': row[1],
                    'code': row[2],
                    "coordinates": coordinates,
                    "pincode" : row[5]
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


class Block:
    def __init__(self, index, transactions, timestamp, previous_hash, nonce=0):
        self.index = index
        self.transactions = transactions
        self.timestamp = timestamp
        self.previous_hash = previous_hash
        self.nonce = nonce

    def compute_hash(self):
        block_string = json.dumps(self.__dict__, sort_keys=True)
        return sha256(block_string.encode()).hexdigest()


class Blockchain:
    difficulty = 4

    def __init__(self):
        self.unconfirmed_transactions = []
        self.chain = []

    def create_genesis_block(self):
        genesis_block = Block(0, [], 0, "0")
        genesis_block.hash = genesis_block.compute_hash()
        try:
            self.chain = joblib.load("../datasets/wallet.pkl")
            print(self.chain)
        except Exception as e:
            self.chain.append(genesis_block)
            joblib.dump(self.chain,"../datasets/wallet.pkl")

    @property
    def last_block(self):
        return self.chain[-1]

    def add_block(self, block, proof):
        previous_hash = self.last_block.hash

        if previous_hash != block.previous_hash:
            return False

        if not Blockchain.is_valid_proof(block, proof):
            return False

        block.hash = proof
        self.chain.append(block)
        joblib.dump(self.chain,"../datasets/wallet.pkl")

        return True

    def proof_of_work(self, block):
        block.nonce = 0

        computed_hash = block.compute_hash()
        while not computed_hash.startswith('0' * Blockchain.difficulty):
            block.nonce += 1
            computed_hash = block.compute_hash()

        return computed_hash

    def add_new_transaction(self, transaction):
        self.unconfirmed_transactions.append(transaction)

    @classmethod
    def is_valid_proof(cls, block, block_hash):
        return (block_hash.startswith('0' * Blockchain.difficulty) and
                block_hash == block.compute_hash())

    @classmethod
    def check_chain_validity(cls, chain):
        result = True
        previous_hash = "0"

        for block in chain:
            block_hash = block.hash
            delattr(block, "hash")

            if not cls.is_valid_proof(block, block.hash) or \
                    previous_hash != block.previous_hash:
                result = False
                break

            block.hash, previous_hash = block_hash, block_hash

        return result

    def mine(self):
        if not self.unconfirmed_transactions:
            return False

        last_block = self.last_block

        new_block = Block(index=last_block.index + 1,
                          transactions=self.unconfirmed_transactions,
                          timestamp=time.time(),
                          previous_hash=last_block.hash)

        proof = self.proof_of_work(new_block)
        self.add_block(new_block, proof)
        print(len(self.chain))
        self.unconfirmed_transactions = []
        return new_block.index

blockchain = Blockchain()
blockchain.create_genesis_block()

@app.route('/new_transaction', methods=['POST'])
def new_transaction():
    tx_data = request.get_json()
    print(request.get_json())
    required_fields = ["sender", "receiver", "amount"]

    #for field in required_fields:
    #    if not tx_data.get(field):
    #        return jsonify(error=404)

    tx_data["timestamp"] = time.time()

    blockchain.add_new_transaction(tx_data)
    return jsonify(success=200)

@app.route('/chain', methods=['GET'])
def get_chain():
    chain_data = []
    for block in blockchain.chain:
        chain_data.append(block.__dict__)
    return json.dumps({"length": len(chain_data),
                       "chain": chain_data,})

@app.route('/calculateWalletAmount/<uid>',methods=['GET'])
def calculate_amount(uid):
    chain_data = []
    amount = 0
    for block in blockchain.chain:
        chain_data.append(block.__dict__)
    for data in chain_data:
        try:
            if data['transactions'][0]['sender'] == uid:
                amount = amount - data['transactions'][0]['amount']
        except Exception as e:
            continue
        try:
            if data['transactions'][0]['receiver'] == uid:
                amount = amount + data['transactions'][0]['amount']
        except Exception as e:
            continue

    return (jsonify(walletAmount = amount))

@app.route('/mine', methods=['GET'])
def mine_unconfirmed_transactions():
    result = blockchain.mine()
    if not result:
        return "No transactions to mine"
    return jsonify(Success="true")

def create_chain_from_dump(chain_dump):
    generated_blockchain = Blockchain()
    generated_blockchain.create_genesis_block()
    for idx, block_data in enumerate(chain_dump):
        if idx == 0:
            continue
        block = Block(block_data["index"],
                      block_data["transactions"],
                      block_data["timestamp"],
                      block_data["previous_hash"],
                      block_data["nonce"])
        proof = block_data['hash']
        added = generated_blockchain.add_block(block, proof)
        if not added:
            raise Exception("The chain dump is tampered!!")
    return generated_blockchain

@app.route('/add_block', methods=['POST'])
def verify_and_add_block():
    block_data = request.get_json()
    block = Block(block_data["index"],
                  block_data["transactions"],
                  block_data["timestamp"],
                  block_data["previous_hash"],
                  block_data["nonce"])

    proof = block_data['hash']
    added = blockchain.add_block(block, proof)

    if not added:
        return jsonify(code=400)

    return jsonify(code=200)


@app.route('/pending_tx')
def get_pending_tx():
    return json.dumps(blockchain.unconfirmed_transactions)

if __name__ == '__main__':
    app.run()
list

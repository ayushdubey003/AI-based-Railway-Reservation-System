from flask import Flask, jsonify
import csv
app = Flask(__name__)

# with open('./stations.csv', 'rt') as file:
#     reader = csv.reader(file)
#     # for row in reader:
#     #     print(row)


@app.route("/")
def home():
    return 'hii...there 3!!!'

@app.route("/stationslistversion", methods=['GET'])
def stationslistversion():
    ans={
        "version":"1.0.0"
    }
    return jsonify(ans)

@app.route("/level/<code>")
def level(code):
    return jsonify(name=code)


@app.route("/trains/<board>/<destination>", methods=['GET'])
def listoftrains(board, destination):
    ans = []
    with open('../datasets/trains.csv', 'rt') as file:
        reader = csv.reader(file)
        for row in reader:
            flag = 0
            med_stations = row[7]
            sep_stations = med_stations.split('$')
            for med in sep_stations:
                if med == board:
                    if flag == 1:
                        ans.append({
                            "train no": row[0],
                            "train name": row[1],
                            "running days": row[2],
                            "available classes": row[3],
                            "type": row[4],
                            "zone": row[5],
                            "route with names": row[6],
                            "route with codes": row[7],
                            "arrival times": row[7],
                            "departure times": row[8]
                        })
                    flag += 1
                if med == destination:
                    if flag == 1:
                        ans.append({
                            "train no": row[0],
                            "train name": row[1],
                            "running days": row[2],
                            "available classes": row[3],
                            "type": row[4],
                            "zone": row[5],
                            "route with names": row[6],
                            "route with codes": row[7],
                            "arrival times": row[7],
                            "departure times": row[8]
                        })
                    flag += 1
                if flag == 2:
                    break

    return jsonify(trains=ans)


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
                ans.append({
                    'name': row[0],
                    'code': row[1]
                })

    return jsonify(stations=ans)


if __name__ == '__main__':
    app.run()
list
from flask import Flask, jsonify
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


if __name__ == '__main__':
    app.run()
list
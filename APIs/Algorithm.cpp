# include <bits/stdc++.h>
# include <boost/algorithm/string.hpp>
# include <chrono>
using namespace std;
using namespace std::chrono;

struct data{
  string sourceStation;
  string destinationStation;
  int sourceDeparture;
  int destinationArrival;
  string trainNumber;
  string days;
};

struct queueData{
  int startingTime;
  int endingTime;
  int lastDeparture;
  string lastTrain;
  string lastStation;
  vector <int> time;
  vector <string> trainList;
  vector <string> stationList;
  int intermissions;
  int duration;
  int type;
};

bool comparator(struct queueData queueData1, struct queueData queueData2){
  return queueData1.duration < queueData2.duration;
}

int distanceInKM(string latitude1, string longitude1, string latitude2, string longitude2){
  int R = 6371;
  double lat1 = stoi(latitude1) / 10000.0;
  double long1 = stoi(longitude1) / 10000.0;
  double lat2 = stoi(latitude2) / 10000.0;
  double long2 = stoi(longitude2) / 10000.0;
  double latitudeDifference = fabs(lat1 - lat2) * (3.1416 / 180);
  double longitudeDifference = fabs(long1 - long2) * (3.1416 / 180);
  double a = sin(latitudeDifference / 2) * sin(latitudeDifference / 2) + cos(lat1 * 3.1416 / 180) * cos(lat2 * 3.1416 / 180) * sin(longitudeDifference / 2) * sin(longitudeDifference / 2);
  double c = 2 * atan2(sqrt(a),sqrt(1-a));
  int d = R * c;
  return d;
}

int main(int argc, char *argv[]){
  // auto start = high_resolution_clock::now();

  freopen("result.txt", "w", stdout);
  string source = argv[1];
  string destination = argv[2];
  int intermediateStation = stoi(argv[3]);
  int currentDay = stoi(argv[4]);
  int minTime = stoi(argv[5]);
  int maxTime = stoi(argv[6]);


  fstream stationFile;
  fstream trainFile;
  fstream routesFile;
  fstream arrivalFile;
  fstream departureFile;
  fstream runningFile;
  fstream latLongFile;
  fstream latLongFullFile;

  string stationFileName;
  string trainFileName;
  string routesFileName;
  string arrivalFileName;
  string departureFileName;
  string runningFileName;
  string latLongFileName;
  string latLongFullFileName;

  stationFileName = "../datasets/stationCodes.txt";
  trainFileName = "../datasets/reservedTrains.txt";
  routesFileName = "../datasets/routes.txt";
  arrivalFileName = "../datasets/arrival.txt";
  departureFileName = "../datasets/departure.txt";
  runningFileName = "../datasets/runningDays.txt";
  latLongFileName = "../datasets/latlong.txt";
  latLongFullFileName = "../datasets/latlongFull.txt";

  stationFile.open(stationFileName.c_str());
  trainFile.open(trainFileName.c_str());
  routesFile.open(routesFileName.c_str());
  arrivalFile.open(arrivalFileName.c_str());
  departureFile.open(departureFileName.c_str());
  runningFile.open(runningFileName.c_str());
  latLongFile.open(latLongFileName.c_str());
  latLongFullFile.open(latLongFullFileName.c_str());

  string stationCode;
  string trainNumber;
  string routeString;
  string arrivalString;
  string departureString;
  string runningDay;
  string latLong;
  string sourceLatitude;
  string sourceLongitude;
  string destinationLatitude;
  string destinationLongitude;

  vector <string> stationCodes;
  vector <string> trainNumbers;
  vector <string> routes[2500];
  vector <string> arrivalTime[2500];
  vector <string> departureTime[2500];
  vector <string> runningDays;
  vector <string> stationData[3600];
  vector <string> nearbySource;
  vector <string> nearbyDestination;
  vector <struct data> graph[9000];
  vector <struct queueData> solution;

  map <string, int> stationMap;

  queue <struct queueData> bfsQueue;

  int i, j, k;
  int stationSize;
  int index;
  int startDay;
  int arrival;
  int departure;
  int arrivalDay;
  int stationDataSize;
  int routeSize = 0;
  int nearbySourceSize = 0;
  int nearbyDestinationSize = 0;
  int leastDuration;


  i = 0;
  while(stationFile >> stationCode){
    stationCodes.push_back(stationCode);
    stationMap[stationCode] = i;
    i++;
  }

  while(trainFile >> trainNumber){
    trainNumbers.push_back(trainNumber);
  }

  i = 0;
  while(routesFile >> routeString){
    boost::split(routes[i], routeString, boost::is_any_of("$"));
    i++;
  }

  i = 0;
  while(arrivalFile >> arrivalString){
    boost::split(arrivalTime[i], arrivalString, boost::is_any_of("$"));
    i++;
  }

  i = 0;
  while(departureFile >> departureString){
    boost::split(departureTime[i], departureString, boost::is_any_of("$"));
    i++;
  }

  while(runningFile >> runningDay){
    runningDays.push_back(runningDay);
  }

  i = 0;
  while(latLongFile >> latLong){
    stationData[i].push_back(latLong);
    latLongFile >> latLong;
    stationData[i].push_back(latLong);
    latLongFile >> latLong;
    stationData[i].push_back(latLong);
    i++;
  }

  sourceLatitude = "";
  destinationLatitude = "";
  while(latLongFullFile >> latLong){
    if(source == latLong){
      latLongFullFile >> latLong;
      sourceLatitude = latLong;
      latLongFullFile >> latLong;
      sourceLongitude = latLong;
      continue;
    }
    if(destination == latLong){
      latLongFullFile >> latLong;
      destinationLatitude = latLong;
      latLongFullFile >> latLong;
      destinationLongitude = latLong;
      continue;
    }
    latLongFullFile >> latLong;
    latLongFullFile >> latLong;
  }

  for (i = 0; i < 2473; i++){
    stationSize = routes[i].size() - 1;
    for(j = 0; j < stationSize - 1; j++){
      struct data temp;
      temp.sourceStation = routes[i][j];
      temp.trainNumber = trainNumbers[i];
      temp.days = runningDays[i];
      temp.sourceDeparture = stoi(departureTime[i][j]);

      if (temp.sourceDeparture == -1)
        continue;

      index = stationMap[temp.sourceStation];
      for(k = j + 1; k < stationSize; k++){
        temp.destinationStation = routes[i][k];
        temp.destinationArrival = stoi(arrivalTime[i][k]);

        if (temp.destinationArrival == -1)
          continue;

        graph[index].push_back(temp);
      }
    }
  }

  index = stationMap[source];

  for (auto node : graph[index]){
    struct queueData temp;
    temp.startingTime = node.sourceDeparture;
    temp.endingTime = node.destinationArrival;
    temp.lastTrain = node.trainNumber;
    temp.trainList.push_back(temp.lastTrain);
    temp.lastStation = node.destinationStation;
    temp.stationList.push_back(source);
    temp.stationList.push_back(temp.lastStation);
    temp.lastDeparture = temp.startingTime;
    temp.time.push_back(temp.lastDeparture);
    temp.intermissions = 0;

    startDay = currentDay - temp.startingTime / (24 * 60) + 7;
    startDay %= 7;

    if (node.days[startDay] == '0')
      continue;

    temp.startingTime += (startDay * 24 * 60);
    temp.endingTime += (startDay * 24 * 60);
    temp.startingTime %= (7 * 24 * 60);
    temp.endingTime %= (7 * 24 * 60);

    bfsQueue.push(temp);
  }

  while(!bfsQueue.empty()){
    struct queueData temp, prev, lastAdded;
    temp = bfsQueue.front();
    bfsQueue.pop();

    if (temp.intermissions >= intermediateStation)
      continue;

    index = stationMap[temp.lastStation];
    arrival = temp.endingTime;
    arrivalDay = arrival / (24 * 60);
    prev = temp;

    for (auto node : graph[index]){
      temp = prev;

      departure = node.sourceDeparture;
      startDay = arrivalDay + 7 - departure / (24 * 60);
      startDay %= 7;


      if (node.days[startDay] == '0')
        continue;
      departure = startDay * 24 * 60 + departure;
      departure %= (7 * 24 * 60);
      if (arrival > departure)
        departure += (7 * 24 * 60);
      if (departure - arrival > maxTime || departure - arrival < minTime)
        continue;
      if (temp.lastTrain == node.trainNumber)
        continue;
      departure %= (7 * 24 * 60);

      temp.time.push_back((departure - temp.lastDeparture + 7 * 24 * 60) % (7 * 24 * 60));
      temp.lastDeparture = departure;
      temp.endingTime = node.destinationArrival + startDay * 24 * 60;
      temp.endingTime %= (7 * 24 * 60);
      temp.lastTrain = node.trainNumber;
      temp.trainList.push_back(temp.lastTrain);
      temp.lastStation = node.destinationStation;
      temp.stationList.push_back(temp.lastStation);

      if (temp.lastStation == destination){
        if (temp.startingTime > temp.endingTime)
          temp.endingTime += (7 * 24 * 60);
        temp.duration = temp.endingTime - temp.startingTime;

        if (routeSize > 0)
          lastAdded = solution[routeSize - 1];

        if (temp.stationList == lastAdded.stationList || temp.trainList == lastAdded.trainList){
          if (temp.duration >= lastAdded.duration)
            continue;
          else{
            solution.pop_back();
            routeSize--;
          }
        }
        temp.type = 1;
        solution.push_back(temp);
        routeSize++;
        continue;
      }

      temp.intermissions += 1;
      bfsQueue.push(temp);
    }
  }

  sort(solution.begin(), solution.end(), comparator);
  if(solution.size() > 10)
    solution.resize(10);

  if(sourceLatitude != "" && destinationLatitude != ""){
    i = 0;
    while(stationData[i].size() != 0){
      if(nearbySourceSize < 5 && distanceInKM(sourceLatitude, sourceLongitude, stationData[i][1], stationData[i][2]) < 75){
        nearbySource.push_back(stationData[i][0]);
        nearbySourceSize++;
      }
      if(nearbyDestinationSize < 5 && distanceInKM(destinationLatitude, destinationLongitude, stationData[i][1], stationData[i][2]) < 75){
        nearbyDestination.push_back(stationData[i][0]);
        nearbyDestinationSize++;
      }
      i++;
    }
  }

  for (auto alternateSource : nearbySource){
    index = stationMap[alternateSource];
    leastDuration = INT_MAX;
    struct queueData temp, prev;
    for(auto node : graph[index]){
      if(node.destinationStation == destination){
        if(node.destinationArrival - node.sourceDeparture < leastDuration){
          startDay = currentDay - node.sourceDeparture / (24 * 60) + 7;
          startDay %= 7;

          if (node.days[startDay] == '0')
            continue;

          temp = prev;
          temp.stationList.push_back(alternateSource);
          temp.stationList.push_back(destination);
          temp.trainList.push_back(node.trainNumber);
          temp.duration = node.destinationArrival - node.sourceDeparture;
          leastDuration = temp.duration;
          temp.time.push_back(node.sourceDeparture);
          temp.type = 3;
        }
      }
    }
    if(temp.trainList.size() != 0)
      solution.push_back(temp);
  }

  for (auto node : solution){

    for(auto station : node.stationList)
      cout << station << " ";
    cout << endl;

    for(auto train : node.trainList)
      cout << train << " ";
    cout << endl;

    cout << node.duration << endl;

    for(auto timeDuration : node.time)
      cout << timeDuration << " ";
    cout << endl;

    cout<< node.type <<endl;
  }

  // auto stop = high_resolution_clock::now();
  // auto duration = duration_cast <microseconds> (stop - start);
  // cout << duration.count();
}

# include <bits/stdc++.h>
# include <boost/algorithm/string.hpp>
// # include <chrono>
using namespace std;
// using namespace std::chrono;

int main(){
  // auto start = high_resolution_clock::now();

  fstream stationFile;
  fstream trainFile;
  fstream routesFile;
  fstream arrivalFile;
  fstream departureFile;
  fstream runningFile;

  string stationFileName;
  string trainFileName;
  string routesFileName;
  string arrivalFileName;
  string departureFileName;
  string runningFileName;

  stationFileName = "datasets/stationCodes.txt";
  trainFileName = "datasets/reservedTrains.txt";
  routesFileName = "datasets/routes.txt";
  arrivalFileName = "datasets/arrival.txt";
  departureFileName = "datasets/departure.txt";
  runningFileName = "datasets/runningDays.txt";

  stationFile.open(stationFileName.c_str());
  trainFile.open(trainFileName.c_str());
  routesFile.open(routesFileName.c_str());
  arrivalFile.open(arrivalFileName.c_str());
  departureFile.open(departureFileName.c_str());
  runningFile.open(runningFileName.c_str());

  string stationCode;
  string trainNumber;
  string routeString;
  string arrivalString;
  string departureString;
  string runningDay;

  vector <string> stationCodes;
  vector <string> trainNumbers;
  vector <string> routes[2500];
  vector <string> arrivalTime[2500];
  vector <string> departureTime[2500];
  vector <string> runningDays;

  int i;

  while(stationFile >> stationCode){
    stationCodes.push_back(stationCode);
  }
  while(trainFile >> trainNumber){
    trainNumbers.push_back(trainNumber);
  }
  i = 0;
  while(routesFile >> routeString){
    boost::split(routes[i], routeString, boost::is_any_of("$"));
    i++;
  }
  cout<<i<<endl;
  i = 0;
  while(arrivalFile >> arrivalString){
    boost::split(arrivalTime[i], arrivalString, boost::is_any_of("$"));
  }
  // i = 0;
  // while(departureFile >> departureString){
  //   boost::split(departureTime[i], departureString, boost::is_any_of("$"));
  //   i++;
  // }
  while(runningFile >> runningDay){
    runningDays.push_back(runningDay);
  }

   // auto stop = high_resolution_clock::now();
   // auto duration = duration_cast<microseconds>(stop - start);
   // cout << duration.count();
}

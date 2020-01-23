import csv

f=open("stationNames.txt","r")
r=[]
r.append("Station Names")
r.append("Station Codes")

x=f.read()
f.close()

with open("stations.csv", "w") as writeFile:
	file = csv.writer(writeFile)
	file.writerow([r[0]] + [r[1]])


u=x.split(",")
for i in range(0,len(u)):
	v = u[i].split("-")
	v[1]=v[1].strip()
	v[2]=v[2].strip()
	z =  v[2].split("\\")
	z[0] = z[0].strip()
	with open("stations.csv", "a") as writeFile:
		file = csv.writer(writeFile)
		file.writerow([v[1]] + [z[0]])

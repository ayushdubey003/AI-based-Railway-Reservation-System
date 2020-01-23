import requests 
from bs4 import BeautifulSoup 
import time
import csv

r = []
r.append("Train Number")
r.append("Train Name")
r.append("Running Days")
r.append("Available Classes")
r.append("Type")
r.append("Zone")
r.append("Route with names")
r.append("Route with codes")
r.append("Arrival Times")
r.append("Departure Times")

# with open("trains.csv", "w", newline='') as writeFile:
#     f = csv.writer(writeFile)
#     f.writerow([r[0]] + [r[1]] + [r[2]] + [r[3]] + [r[4]] + [r[5]] + [r[6]] + [r[7]] + [r[8]] + [r[9]])
# writeFile.close()

f = open("trainNumbers.txt","r")
u=f.read()
v=u.split("\n")

for i in range(10691,len(v)):
	s = str(v[i])
	url = "https://etrain.info/in?TRAIN="+s
	print(i)

	try:
		content = requests.get(url)
		soup = BeautifulSoup(content.text, 'html.parser')

		try:
			even_junctions = soup.find('table',{"id":"schtbl"}).find_all('tr',{"class":"even"})
			odd_junctions = soup.find('table',{"id":"schtbl"}).find_all('tr',{"class":"odd"})

			named_routes=""
			coded_routes=""
			arrival_times=""
			dept_times=""
			for j in range(0,1000):
				try:
					ej = even_junctions[j].find_all('td')
					coded_routes=coded_routes + ej[1].text + "$"
					named_routes = named_routes + ej[2].text + "$"
					arrival_times = arrival_times + ej[3].text + "$"
					dept_times = dept_times + ej[4].text + "$"
					try:
						oj = odd_junctions[j+2].find_all('td')
						coded_routes=coded_routes + oj[1].text + "$"
						named_routes = named_routes + oj[2].text + "$"
						arrival_times = arrival_times + oj[3].text + "$"
						dept_times = dept_times + oj[4].text + "$"
					except Exception as e:
						print(e)
				except Exception as e:
					print(e)
					continue

			with open("trains.csv", "a", newline='') as writeFile:
				f = csv.writer(writeFile)
				a=[]
				a.append(s)
				a.append(soup.find('tr',{"class":"trhead udborder"}).find('b').text.split("(")[0].strip())
				a.append(soup.find_all('td',{"class":"nobl"})[0].text.split(":")[1].strip())
				a.append(soup.find_all('td',{"class":"nobl"})[1].text.split(":")[1].strip())
				a.append(soup.find_all('tr',{"class":"even dborder"})[2].find_all('td')[0].text.split(":")[1].strip())
				a.append(soup.find_all('tr',{"class":"even dborder"})[2].find_all('td')[1].text.split(":")[1].strip())
				a.append(named_routes)
				a.append(coded_routes)
				a.append(arrival_times)
				a.append(dept_times)

				f.writerow([a[0]] + [a[1]] + [a[2]] + [a[3]] + [a[4]] + [a[5]] + [a[6]] + [a[7]] + [a[8]] + [a[9]])

			writeFile.close()
		except Exception as e:
			continue
	except Exception as e:
		continue
import csv
from selenium import webdriver
import time
from selenium.webdriver.common.keys import Keys

driver = webdriver.Chrome("/usr/lib/chromium-browser/chromedriver")

url = "https://www.google.co.in"


sn = set()
sc = set()

remsn = []
remsc = []

i=0

with open("../datasets/stations.csv","r") as readFile:
	file = csv.reader(readFile)
	for row in file:
		sn.add(row[0])
		sc.add(row[1].strip())

so = set()
sp = set()

with open("../datasets/stationsModified.csv","r") as readFile:
	file = csv.reader(readFile)
	for row in file:
		so.add(row[0])
		sp.add(row[1])

for rem in (sn-so):
	remsn.append(rem)
	with open("../datasets/stations.csv","r") as readFile:
		file = csv.reader(readFile)
		for row in file:
			if row[0] == rem:
				remsc.append(row[1])
				break



driver.get(url)

for i in range(0,len(remsn)):
	print(remsn[i])
	with open("../datasets/stationsModified.csv", "a") as writeFile:
		try:
			driver.find_elements_by_class_name("gLFyf")[0].clear()
			s = remsn[i] + " India coordinates"
			driver.find_elements_by_class_name("gLFyf")[0].send_keys(s)
			driver.find_elements_by_class_name("gLFyf")[0].send_keys(Keys.ENTER)
			try:
				coordinates = driver.find_elements_by_class_name("Z0LcW")[0].text.encode('utf-8').strip()
				print(coordinates)
				lati = coordinates.split(",")[0].strip()
				longi = coordinates.split(",")[1].strip()
				file = csv.writer(writeFile)
				file.writerow([remsn[i]] + [remsc[i]] + [lati] + [longi])
			except Exception as e:
				print(e)
				file = csv.writer(writeFile)
				file.writerow([remsn[i]] + [remsc[i]] + ["null"] + ["null"])
				continue
		except Exception as e:
			print(e)
			driver.quit()
			driver = webdriver.Chrome("/usr/lib/chromium-browser/chromedriver")
			driver.get(url)
			i = i-1
			continue

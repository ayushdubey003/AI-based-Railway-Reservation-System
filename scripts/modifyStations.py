import csv
from selenium import webdriver
import time
from selenium.webdriver.common.keys import Keys

driver = webdriver.Chrome("/usr/lib/chromium-browser/chromedriver")

url = "https://www.google.co.in"

stationNames = []
stationCodes = []

with open("../datasets/stations.csv","r") as file:
	read = csv.reader(file)
	for row in read:
		stationNames.append(row[0])
		stationCodes.append(row[1])

driver.get(url)

for i in range(0,len(stationNames)):
	print(i)
	with open("../datasets/stationsModified.csv", "a") as writeFile:
		try:
			driver.find_elements_by_class_name("gLFyf")[0].clear()
			s = stationNames[i] + " India coordinates"
			driver.find_elements_by_class_name("gLFyf")[0].send_keys(s)
			driver.find_elements_by_class_name("gLFyf")[0].send_keys(Keys.ENTER)
			try:
				coordinates = driver.find_elements_by_class_name("Z0LcW")[0].text.encode('utf-8').strip()
				print(coordinates)
				lati = coordinates.split(",")[0].strip()
				longi = coordinates.split(",")[1].strip()
				file = csv.writer(writeFile)
				file.writerow([stationNames[i]] + [stationCodes[i]] + [lati] + [longi])
			except Exception as e:
				print(e)
				file = csv.writer(writeFile)
				file.writerow([stationNames[i]] + [stationCodes[i]] + ["null"] + ["null"])
				continue
		except Exception as e:
			print(e)
			driver.quit()
			driver = webdriver.Chrome("/usr/lib/chromium-browser/chromedriver")
			driver.get(url)
			i = i-1
			continue

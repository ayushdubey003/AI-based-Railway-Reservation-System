import csv
from selenium import webdriver
import time
from selenium.webdriver.common.keys import Keys

driver = webdriver.Chrome("/usr/lib/chromium-browser/chromedriver")

reserved_trains = set()

with open("../datasets/reservedTrains.csv","r") as readFile:
	file = csv.reader(readFile)
	for row in file:
		reserved_trains.add(row[1])

url = "https://enquiry.indianrail.gov.in/ntes/index.html"

while(True):
	try:
		driver.get(url)
		driver.find_element_by_id("ui-id-9").click()
		break
	except Exception as e:
		continue

with open("../datasets/routesData.csv","w") as writeFile:
	file = csv.writer(writeFile)
	file.writerow(["Train Number"]+["Train Name"]+["Arrival Delays"]+["Departure Delays"])

with open("../datasets/combinedtrains.csv","r") as readFile:
	file = csv.reader(readFile)
	i = 0
	for row in file:
		i = i + 1
		if i == 1:
			continue
		if True:
			print(row[0])
			try:
				driver.get(url)
				driver.find_element_by_id("ui-id-9").click()
				driver.find_element_by_id("avgDelayTrainInput").clear()
				driver.find_element_by_id("avgDelayTrainInput").send_keys(row[0])
				time.sleep(5)
				tr = driver.find_element_by_id("trainScheduleAvgDelayDispTbl").find_elements_by_tag_name("tr")
				arrDelay = ""
				deptDelay = ""
				print(len(tr))
				for j in range(0,len(tr)):
					td = tr[j].find_elements_by_tag_name("td")
					arrDelay = arrDelay + "$" + td[2].text
					deptDelay = deptDelay + "$" + td[3].text
				with open("../datasets/routesData.csv","a") as writeFile:
					file = csv.writer(writeFile)
					file.writerow([row[0]]+[row[1]]+[arrDelay]+[deptDelay])

			except Exception as e:
				print(e)
				with open("../datasets/routesData.csv","a") as writeFile:
					file = csv.writer(writeFile)
					file.writerow([row[0]]+[row[1]]+[""]+[""])
				continue
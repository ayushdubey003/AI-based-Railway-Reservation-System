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

# with open("../datasets/pincodes.csv", "w") as writeFile:
# 	file = csv.writer(writeFile)
# 	file.writerow(["Pincode"])

i = 4214
driver.get(url)

while True:
	i = i+1
	if i >= len(stationCodes):
		break
	with open("../datasets/pincodes.csv", "a") as writeFile:
		try:
			driver.find_elements_by_class_name("gLFyf")[0].clear()
			s = "address of " +stationNames[i] + " Railway Station"
			driver.find_elements_by_class_name("gLFyf")[0].send_keys(s)
			driver.find_elements_by_class_name("gLFyf")[0].send_keys(Keys.ENTER)
			try:
				x = driver.find_elements_by_class_name("Z0LcW")[0].text
				pin = x[len(x)-1]
				print(pin)
				file = csv.writer(writeFile)
				file.writerow([pin])
			except Exception as e:
				print(e)
				file = csv.writer(writeFile)
				file.writerow([""])
				continue
		except Exception as e:
			print(e)
			driver.quit()
			driver = webdriver.Chrome("/usr/lib/chromium-browser/chromedriver")
			driver.get(url)
			i = i-1
			continue
import csv
from selenium import webdriver
import time
from selenium.webdriver.common.keys import Keys

driver = webdriver.Chrome("/usr/lib/chromium-browser/chromedriver")
url = "https://www.google.co.in"

driver.get(url)

stationNames = []

with open("../datasets/stations.csv","r") as file:
	read = csv.reader(file)
	for row in read:
		stationNames.append(row[1])

i = 0
driver.get(url)

while True:
	i = i+1
	with open("../datasets/pincodesLost.csv", "a") as writeFile:
		try:
			driver.find_elements_by_class_name("gLFyf")[0].clear()
			s = stationNames[i] + " pincode"
			driver.find_elements_by_class_name("gLFyf")[0].send_keys(s)
			driver.find_elements_by_class_name("gLFyf")[0].send_keys(Keys.ENTER)
			try:
				x = driver.find_elements_by_class_name("Z0LcW")[0].text
				pin = x
				print(pin)
				# file = csv.writer(writeFile)
				# file.writerow([stationNames[i]]+[pin])
			except Exception as e:
				print(e)
				# file = csv.writer(writeFile)
				# file.writerow([stationNames[i]]+[""])
				continue
		except Exception as e:
			print(e)
			driver.quit()
			driver = webdriver.Chrome("/usr/lib/chromium-browser/chromedriver")
			driver.get(url)
			i = i-1
			continue

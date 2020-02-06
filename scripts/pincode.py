import csv
from selenium import webdriver
import time
from selenium.webdriver.common.keys import Keys

driver = webdriver.Chrome("/usr/lib/chromium-browser/chromedriver")

url = "https://worldpostalcode.com/lookup"

stationNames = []
stationCodes = []

with open("../datasets/stations.csv","r") as file:
	read = csv.reader(file)
	for row in read:
		stationNames.append(row[0])
		stationCodes.append(row[1])

with open("../datasets/pincodes.csv", "w") as writeFile:
	file = csv.writer(writeFile)
	file.writerow(["Pincode"])

i = 1

while True:
	driver.get(url)
	if i >= len(stationCodes):
		break
	with open("../datasets/pincodes.csv", "a") as writeFile:
		try:
			driver.find_element_by_id("search").clear()
			driver.find_element_by_id("search").send_keys(stationNames[i]+" INDIA")
			driver.find_element_by_id("search").send_keys(Keys.ENTER)
			ans = driver.find_element_by_xpath("//*[@id=\"map_canvas\"]/div[1]/div[6]/div/div[1]/div/div[1]/b").text
			print(i)
			file = csv.writer(writeFile)
			file.writerow([str(ans).strip()])
			i = i+1
		except Exception as e:
			file = csv.writer(writeFile)
			file.writerow(["null"])
			continue

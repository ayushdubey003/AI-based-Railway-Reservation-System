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
driver.get(url)

while True:
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
		except Exception as e:
			try:
				u = driver.find_elements_by_class_name("search_units")[0].find_elements_by_class_name("search_unit")[0].find_elements_by_class_name("code")[0].text
				file = csv.writer(writeFile)
				file.writerow([u])
			except Exception as e:
				file = csv.writer(writeFile)
				file.writerow(["null"])
	i = i+1

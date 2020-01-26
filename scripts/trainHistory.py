import requests 
from selenium import webdriver
import csv
import time
from selenium.webdriver.common.keys import Keys
driver = webdriver.Chrome("/usr/lib/chromium-browser/chromedriver")

driver.maximize_window()
trains =[]

with open("../datasets/trains.csv","r") as x:
	file = csv.reader(x)
	i = 0
	for row in file:
		i = i+1
		if i==1:
			continue
		trains.append(row[0].strip())

r = []
r.append("Right Time (0-15 mins)")
r.append("Slight Delay (15-60 mins)")
r.append("Significant Delay(>1 Hour)")
r.append("Cancelled/Unknown")

# with open("../datasets/trainHistory.csv","w") as trainHistory : 
# 	file = csv.writer(trainHistory)
# 	file.writerow([r[0]]+[r[1]]+[r[2]]+[r[3]])

i = 3862

while(True):
	print(trains[i])
	url = "https://etrain.info/in?PAGE=runningHistory--"+trains[i]+"--3m"
	try :
		driver.get(url)
		try:
			i=i+1
			# u = driver.find_element_by_id("visualization").find_elements_by_tag_name("svg")[0].find_elements_by_tag_name("g")
			#008000
			u = driver.find_elements_by_xpath('//*[@id="visualization"]/div/div[1]/div/*[name()="svg"]/*[name()="g"][2]/*[name()="g"][1]/*[name()="g"][2]/*[name()="rect"]')
# //*[@id="visualization"]/div/div[1]/div/svg/g[2]/g[1]/g[2]			
# //*[@id="visualization"]/div/div[1]/div/svg/g[2]/g[1]/g[2]
# //*[@id="visualization"]/div/div[1]/div/svg/g[2]/g[1]/g[2]/rect[1]
			# u.click()
			print(len(u))
			onTime=[]
			slightDelay=[]
			signDelay=[]
			can =[]
			for j in range(0,len(u)):
				s='//*[@id="visualization"]/div/div[1]/div/*[name()="svg"]/*[name()="g"][2]/*[name()="g"][1]/*[name()="g"][2]/*[name()="rect"]';
				s=s+'['+str(j+1)+']'
				if j<len(u)/4:
					onTime.append(driver.find_element_by_xpath(s).get_attribute("width"))
				elif j<len(u)/2:
					slightDelay.append(driver.find_element_by_xpath(s).get_attribute("width"))
				elif j<3*(len(u)/4):
					signDelay.append(driver.find_element_by_xpath(s).get_attribute("width"))
				else:
					can.append(driver.find_element_by_xpath(s).get_attribute("width"))

			ot=""
			sd=""
			sid=""
			ca=""
			for j in range(0,len(onTime)):
				ot=ot+str(float(onTime[j].strip())/498)+"$"
				sd=sd+str(float(slightDelay[j].strip())/498)+"$"
				sid=sid+str(float(signDelay[j].strip())/498)+"$"
				ca=ca+str(float(can[j].strip())/498)+"$"

			with open("../datasets/trainHistory.csv","a") as trainHistory : 
				file = csv.writer(trainHistory)
				file.writerow([ot]+[sd]+[sid]+[ca])

		except Exception as e:
			print(e)
			continue
	except Exception as e:
		print(e)
		continue

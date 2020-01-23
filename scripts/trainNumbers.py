from selenium import webdriver
import time

driver = webdriver.Chrome("/usr/lib/chromium-browser/chromedriver")

url = "https://www.nativeplanet.com/trains-in-india/index-"

for i in range(97,123):
	url1 = url + chr(i)
	driver.get(url1)
	try:
		f = open("trainNumbers.txt","a")
		u = driver.find_elements_by_class_name("np-trainsTable")[0].find_elements_by_tag_name("tr")
		for j in range(1,len(u)):
			v = u[j].find_elements_by_tag_name("td")[0].text
			f.write(v+"\n")
			print(v)
		f.close()
	except Exception as e:
		print(e)
		exit(1)
		f.close()

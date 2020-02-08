import scrapy
import pkgutil
import re
from datetime import datetime

class PNRSpider(scrapy.Spider):
    name = "PNRStatusScraper"
    data = open("../dataset/trainNumbers.txt","r")
    start_urls=[]
    for train in data:
        train = train.strip()
        start_urls.append("https://indiarailinfo.com/pnr/0?q="+train)

    def parse(self, response):
        url = response.request.url
        num_pattern = re.compile('[0-9]+')
        month_dictionary = {'JAN': 1, 'FEB': 2, 'MAR': 3, 'APR': 4, 'MAY': 5, 'JUN': 6, 'JUL': 7, 'AUG': 8, 'SEP': 9, 'OCT': 10, 'NOV': 11, 'DEC': 12}
        pattern_day = re.compile('[0-9][0-9]')
        pattern_month = re.compile('[A-Z][A-Z][A-Z]')
        pattern_year = re.compile('[0-9][0-9][0-9][0-9]')

        My_table = response.xpath('//div[@class="clearfix blogunit"]')
        numberOfDivisions = len(My_table)
        trainNumber = response.request.url
        length = len(trainNumber)
        i = 0
        while i < length and trainNumber[i] != '=':
            i += 1
        trainNumber = trainNumber[i+1:]
        if trainNumber == "" or trainNumber == None:
            return
        for tab in My_table:
            try:
                divText= ''.join(tab.xpath('.//div[@style="color:#C100A7;"]//text()').getall())
                departureDate = divText[13:25]
                availableSeats = 0
                length = len(divText)
                i = 0
                timeList = []
                additionalNightList = []
                while i < length:
                    if divText[i] == '@':
                        time = divText[i+2:i+7]
                        timeList.append(time)
                        add = '0'
                        if divText[i+8] == '+':
                            add = divText[i+9]
                        additionalNightList.append(add)
                    i = i +1
                departureTime = timeList[0]
                arrivalTime = timeList[1]
                additionalNights = additionalNightList[1]
                i = 0
                departureStation = ""
                arrivalStation = ""
                while i < length:
                    if divText[i:i+4] == "Dep:":
                        i = i + 5
                        while divText[i] != '/':
                            departureStation += divText[i]
                            i = i + 1
                    if divText[i:i+4] == "Arr:":
                        i = i + 5
                        while divText[i] != '/':
                            arrivalStation += divText[i]
                            i = i + 1
                    if divText[i:i+6] == "Class:":
                        i = i + 6
                        while divText[i:i+3] != " - ":
                            i = i + 1
                        classType = divText[i+3:i+5]
                    if divText[i:i+5] == "Avbl-":
                        i = i + 5
                        availableSeats = divText[i:i+4]
                        break
                    i = i + 1
                i = 0
                dateList = []
                timeList = []
                for dates in tab.xpath('.//div[@style="color:#C40000;"]'):
                    string = ''.join(dates.xpath('.//text()').getall())
                    length = len(string)
                    i = 0
                    date = ""
                    while i < length and string[i] != ':':
                        i += 1
                    i += 2
                    while i < length and string[i] != '(':
                        date += string[i]
                        i += 1
                    date = date.strip()
                    dateList.append(date)
                    time = ""
                    i += 1
                    while i < length and string[i] != ')':
                        time += string[i]
                        i += 1
                    timeList.append(time)
                statusTable = tab.xpath('.//span[@class="pnravl" or @class="pnrnavl" or @class="pnrrac" or @class="pnrwl"]')
                passengerText = ''.join(tab.xpath('.//text()').getall())
                statusText = statusTable.xpath('.//text()').getall()
                statusClass = statusTable.xpath('.//@class').getall()
                length = len(passengerText)
                i = 0
                maximumPassengerNumber = 0
                numberOfPassengers = 0
                while i < length:
                    if passengerText[i:i+10]=="Passenger ":
                        try:
                            passengerNumber = int(passengerText[i+10])
                            if maximumPassengerNumber < passengerNumber:
                                maximumPassengerNumber = passengerNumber
                                numberOfPassengers += 1
                            else:
                                break
                        except Exception as e:
                            print(e, "uu")
                    i += 1
                length = len(dateList)
                totalNumberOfStatus = length * numberOfPassengers
                length = len(statusTable)
                statusList = []
                i = 0
                j = 0

                if length > totalNumberOfStatus:
                    for status in statusText:
                        if i % 2 == 0:
                            if statusClass[j] == "pnravl":
                                status = "CNF"
                            if statusClass[j] == "pnrnavl":
                                status = "CAN"
                            statusList.append(status)
                        i += 1
                        j += 1
                else:
                    for status in statusText:
                        if statusClass[j] == "pnravl":
                            status = "CNF"
                        if statusClass[j] == "pnrnavl":
                            status = "CAN"
                        statusList.append(status)
                        j += 1
                i = 0
                initialStatus = []
                latestStatus = []
                for status in statusList:
                    pattern = re.compile('GNWL#?:?\s*[0-9]+', re.IGNORECASE)
                    if(pattern.match(status) != None):
                        if int(num_pattern.findall(status)[0]) > 0:
                            status = 'GNWL {}'.format(num_pattern.findall(status)[0])

                    pattern = re.compile('Waitlist#?:?\s*[0-9]+', re.IGNORECASE)
                    if(pattern.match(status) != None):
                        if int(num_pattern.findall(status)[0]) > 0:
                            status = 'GNWL {}'.format(num_pattern.findall(status)[0])

                    pattern = re.compile('WL#?:?\s*[0-9]+', re.IGNORECASE)
                    if(pattern.match(status) != None):
                        if int(num_pattern.findall(status)[0]) > 0:
                            status = 'GNWL {}'.format(num_pattern.findall(status)[0])

                    pattern = re.compile('Waiting#?:?\s*WL:\s*[0-9]+', re.IGNORECASE)
                    if(pattern.search(status) != None):
                        if int(num_pattern.findall(status)[0]) > 0:
                            status = 'GNWL {}'.format(num_pattern.findall(status)[0])

                    pattern = re.compile('RLWL#?:?\s*[0-9]+', re.IGNORECASE)
                    if(pattern.search(status) != None):
                        if int(num_pattern.findall(status)[0]) > 0:
                            status = 'RLWL {}'.format(num_pattern.findall(status)[0])

                    pattern = re.compile('TQWL#?:?\s*[0-9]+', re.IGNORECASE)
                    if(pattern.search(status) != None):
                        if int(num_pattern.findall(status)[0]) > 0:
                            status = 'TQWL {}'.format(num_pattern.findall(status)[0])

                    pattern = re.compile('PQWL#?:?\s*[0-9]+', re.IGNORECASE)
                    if(pattern.search(status) != None):
                        if int(num_pattern.findall(status)[0]) > 0:
                            status = 'PQWL {}'.format(num_pattern.findall(status)[0])

                    pattern = re.compile('RAC#?:?\s*[0-9]+', re.IGNORECASE)
                    if(pattern.search(status) != None):
                        if int(num_pattern.findall(status)[0]) > 0:
                            status = 'RAC {}'.format(num_pattern.findall(status)[0])

                    if True:
                        if i < numberOfPassengers:
                            if status == "CNF" or status == "CAN":
                                break
                            initialStatus.append(status)
                        else:
                            latestStatus.append(status)
                    i += 1

                j = 0
                initialDate = dateList[0]
                initialTime = timeList[0]
                dateList.reverse()
                timeList.reverse()
                latestStatus.reverse()
                try:
                    date_item_1 = departureDate.upper()
                    date_item_2 = initialDate.upper()
                    hour_1 = int(departureTime[0:2])
                    hour_2 = int(initialTime[0:2])
                    day_1 = int(pattern_day.findall(date_item_1)[0])
                    day_2 = int(pattern_day.findall(date_item_2)[0])
                    month_1 = month_dictionary[(pattern_month.findall(date_item_1)[0])]
                    month_2 = month_dictionary[(pattern_month.findall(date_item_2)[0])]
                    year_1 = int(2020 if pattern_year.findall(date_item_1) == [] else pattern_year.findall(date_item_1)[0])
                    year_2 = int(2020 if pattern_year.findall(date_item_2) == [] else pattern_year.findall(date_item_2)[0])
                    datetime_1 = datetime(year_1, month_1, day_1, hour=hour_1)
                    datetime_2 = datetime(year_2, month_2, day_2, hour=hour_2)
                    departureDate = datetime_1.date().__str__()
                    initialDate = datetime_2.date().__str__()

                    departureTime = datetime_1.time().__str__()[0:5]
                    initialTime = datetime_2.time().__str__()[0:5]
                except Exception as e:
                    continue
                l = -1
                for status in latestStatus:
                    i = int(j/numberOfPassengers)
                    try:
                        if l < i:
                            date_item_3 = dateList[i].upper()
                            hour_3 = int(timeList[i][0:2])
                            day_3 = int(pattern_day.findall(date_item_3)[0])
                            month_3 = month_dictionary[(pattern_month.findall(date_item_3)[0])]
                            year_3 = int(2020 if pattern_year.findall(date_item_3) == [] else pattern_year.findall(date_item_3)[0])
                            datetime_3 = datetime(year_3, month_3, day_3, hour=hour_3)

                            if (datetime_1 - datetime_2).total_seconds() < 0:
                                datetime_2 = datetime_1

                            if (datetime_1 - datetime_3).total_seconds() < 0:
                                datetime_3 = datetime_1

                            dateList[i] = datetime_3.date().__str__()
                            timeList[i] = datetime_3.time().__str__()[0:5]

                            if (datetime_1 - datetime_2).total_seconds() // 3600 == (datetime_1 - datetime_3).total_seconds() // 3600:
                                j += 1
                                continue
                            l += 1
                        yield{
                            'Departure Date' : departureDate,
                            'Departure Time' : departureTime,
                            'Arrival Time' : arrivalTime,
                            'Additional Nights' : additionalNights,
                            'Train Number' : trainNumber,
                            'Departure Station' : departureStation,
                            'Arrival Station' : arrivalStation,
                            'Class' : classType,
                            'Avilable Seats' : availableSeats,
                            'Initial Date' : initialDate,
                            'Initial Time' : initialTime,
                            'Initial Status' : initialStatus[0],
                            'Latest Date' : dateList[i],
                            'Latest Time' : timeList[i],
                            'Latest Status' : status,
                        }
                        if status == "CNF" or status == "CAN":
                            break
                    except Exception as e:
                        print(e, i)

                    j += 1
            except Exception as e:
                print(e, "yo")

        if numberOfDivisions != 0:
            url = url[30:]
            length = len(url)
            i = 0
            while i < length and url[i] != '?':
                i += 1
            url = url[:i]
            url = str(int(url) + 1)
            next_page = "https://indiarailinfo.com/pnr/" + url + "?q=" + trainNumber
            yield response.follow(next_page, self.parse)
        else:
            print("got a zero")

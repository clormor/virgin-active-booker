# Virgin Active Court Booker

A library that books tennis court at [Virgin Active](http://www.virginactive.co.uk/) health clubs.

The library includes a command-line utility, which can be run on Windows, Mac or Linux.

In order to successfully use this utility, you require a valid account on the Virgin Active Member's portal.

## Features

1. List available courts on a given day

2. Book courts on a given day, at a given time

	a. (Optionally) book specific courts

	b. (Optionally) filter for indoor/outdoor courts

	
## Running the command-line utility

1. Build the code

		> git clone git@github.com:clormor/virgin-active-booker.git
		> cd virgin-active-booker
		> ./gradlew installApp
		> cd /build/install/virgin-active-booker/bin/
		
2. Display the help message

		> ./virgin-active-booker -h
		Missing required options: u, p
		usage: virgin-active-booker
		 -b,--book                  book courts
		 -d,--date <date>           the date (relative to today's date). Must be
		                            between 0 and 7 (default: 0)
		 -h,--help                  print this help message
		 -indoor                    match any indoor courts (booking)
		 -l,--list                  list available courts
		 -outdoor                   match any outdoor courts (booking)
		 -p,--password <password>   your member's portal password
		 -t,--time <time>           hour of day to list or book courts (24-hour
		                            format)
		 -u,--username <username>   your member's portal username
		 
3. List any available tennis courts tomorrow

		> ./virgin-active-booker -u <username> -p <password> -l -d 1
		...
		Sun, Mar 23
		--------------------------------
		7:00	--> not available
		8:00	--> 3, 4, 5, 6, A, B, C 
		9:00	--> 3, 5, A, B, C 
		10:00	--> A, B, C 
		11:00	--> A, B, C 
		12:00	--> A, B, C 
		13:00	--> B, C 
		14:00	--> C 
		15:00	--> B, C 
		16:00	--> B, C 
		17:00	--> A, B, C 
		18:00	--> A, B, C 
		19:00	--> A, B, C 
		20:00	--> not available
		21:00	--> not available
		22:00	--> not available

4. Book a court tomorrow, at 9am

		> ./virgin-active-booker -u <username> -p <password> -b -d 1 -t 9
		...
		Court 3 has been booked at 9:00 on Sun, Mar 23

## Advanced Options

1. Specify indoor/outdoor courts when booking

		> ./virgin-active-booker -u <username> -p <password> -b -d 1 -t 9 -outdoor
		...
		Court A has been booked at 9:00 on Sun, Mar 23
		
		> ./virgin-active-booker -u <username> -p <password> -b -d 1 -t 18 -indoor
		...
		no court available
		
2. Specify a specific court to book

		> ./virgin-active-booker -u <username> -p <password> -b -d 1 -t 8 -court 5
		...
		Court 5 has been booked at 8:00 on Sun, Mar 23
		
3. Options can be combined to broaden your selection

		// will book ANY indoor court OR court A
		> ./virgin-active-booker -u <username> -p <password> -b -d 1 -t 8 -indoor -court a


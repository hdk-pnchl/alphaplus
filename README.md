TODO
	Welcome message on banner
	mail
		Welcome mail on signup
		forgot password link
		Question answers
			on sending a message also drop a mail
		error message instead of alert
	Wizzard
		for formatting
	Message
		Message will be a thread. Its will contain a subject.
		Message will have a collection of question(And question will have a answer)
	Add date in message list
	profile, replace age with DOB
	Add trans-gender
	Multiple fields in contact No
	Keep mobile no strict.

URL
	http://localhost:8080/alphaplus/static
	http://104.238.126.194:8080/alphaplus/static
	http://www.trainbrainstudio.com

Email ID: hemanshu.panchal@alphaplusindia.com
Password: alpha123


jobDetail= {
	basic: {
		client:""
		instructions:""
		name:""
		no:""
		receivedDate:""
		receivedTime:""
		targetDate:""
		targetTime:""
	},	
	delivery: {
		client:""
		instructions:""
		name:""
		no:""
		receivedDate:""
		receivedTime:""
		targetDate:""
		targetTime:""
	}
}

jobWizzard{
	commonData: {
		
	},
	wizzardData: {
		basic: {
			
		},
		delivery: {
			
		},
		functional: {
			
		},
		plate: {
			
		}
	},
	wizzardStepData: {
		data: {
			client: "",
			instructions: "",
			name: "",
			no: "",
			receivedDate: "",
			receivedTime: "",
			targetDate: "",
			targetTime: ""
		}
		"formDesc": "Enter basic detail:",
		"isHidden": false,
		"name": "basic",
		"modalDataObj": "basic",
		"fieldAry": [{
				"name": "name",
				"label": "Name",
				"modalData": "basic.name",
				"type": "text"
			},{
				"name": "no",
				"label": "No.",
				"modalData": "basic.no",
				"type": "text"
			},{
				"name": "receivedDate",
				"label": "Receiving Date",
				"modalData": "basic.receivedDate",
				"type": "date",
				"format": "dd-MMMM-yyyy"
			},{
				"name": "receivedTime",
				"label": "Receiving Time",
				"modalData": "basic.receivedTime",
				"type": "text"
			},{
				"name": "targetDate",
				"label": "Target Date",
				"modalData": "basic.targetDate",
				"type": "date",
				"format": "dd-MMMM-yyyy"
			},{
				"name": "targetTime",
				"label": "Target Time",
				"modalData": "basic.targetTime",
				"type": "text"
			},{
				"name": "client",
				"label": "Client",
				"modalData": "basic.client",
				"type": "text"
			},{
				"name": "instructions",
				"label": "Instructions",
				"modalData": "basic.instructions",
				"type": "text"
			}
		]
	}
}
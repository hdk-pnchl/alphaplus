
/* -----------------| Fucntion |----------------- */
function Two(){
};
Two.p1= "12345";
Two.m1= function(arg1){
	if(!arg1){
		arg1= Two.p1;
	}
	console.log(this.toString());
	console.log(this.name);
	console.log(arg1);
};
Two.m1("xyz");
Two.m1();

/* -----------------| Class |----------------- */

function One(){
};
One.prototype.p1= "12345";
One.prototype.m1= function(arg1){
	if(!arg1){
		arg1= One.prototype.p1;
	}
	console.log(this.toString());
	console.log(this.name);
	console.log(arg1);
};
//One.m1("xyz");
//One.m1();

/* -----------------| Inheritance |----------------- */

// 001: Basic Parent and Child Object

var obj1= {
	prop1: 1234,
	print: function(){
		console.log(this.toString());
		console.log(this.constructor);
		console.log(this.prop1);
	}
};
obj1.print();
var obj2= Object.create(obj1);
obj2.prop1= "xxxxx";
obj2.print();

// 002: Override method in Child Object

var obj3= Object.create(obj1);
obj3.print= function(argument){
	console.log(this.toString());
	console.log(this.constructor);
	console.log(this.prop1+"!!");
}
obj3.print();

var obj4= Object.create(obj1);
obj4.print= function(argument){
	obj1.print();
}
obj4.print();

var obj5= Object.create(obj1);
obj5.prop1= "hdk.pnchl";
obj5.print= function(argument){
	obj1.print.call(this);
}
obj5.print();

/*
	*** Here:
	obj1 		==> 	Class
	Obj2 		==> 	Object
	Obj3/4/5 	==> 	Sub Class/Object

*/

var Class1= {
	constructor: function (arg1, arg2){
		this.__prop1= arg1;
		this.__prop2= arg2;
	},
	printNice: function(){
		console.log(this.printNice.name+":: "+this.__prop1+" AND "+this.__prop2);
	}
};

var NObj1= Object.create(Class1);
NObj1.constructor("Heath", "Ledger");
NObj1.printNice();


/* -----------------|-----------------| Class: Classical-Model |-----------------|----------------- */

function Answer(arg1, arg2) {
	this.__prop1= arg1;
	this.__prop2= arg2;
	this.getProp1= function(){
		return this.__prop1;
	};
	this.getProp2= function(){
		return this.__prop2;
	};
	this.print= function(){
		console.log("Good morning World!");
		console.log(this.getProp1());
		console.log(this.getProp2());
	};
}

Answer.prototype.getProp1= function(){
	return this.__prop1;
};
Answer.prototype.getProp2= function(){
	return this.__prop2;
};
Answer.prototype.print= function(){
	console.log("Good morning World!");
	console.log(this.getProp1());
	console.log(this.getProp2());
};

var ans1= new Answer(123, "xyz");
ans1.print();


/* -----------------| Inheritance |----------------- */


function GoodAnswer(arg1, arg2, arg3){
	Answer.call(this, arg1, arg2);
	this.__prop3= arg3;
	console.log(arg3);
}
GoodAnswer.prototype= Object.create(Answer.prototype);
GoodAnswer.prototype.constructor= GoodAnswer;

GoodAnswer.prototype.getProp3= function(){
	return this.__prop3;
};
GoodAnswer.prototype.print= function(){
	Answer.print.call(this);
	console.log(this.getProp3());
};

var goodAnswer1= new GoodAnswer("Batman", "is", "Heath--Ledger");
goodAnswer1.print();













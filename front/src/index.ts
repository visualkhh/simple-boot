
// Fixtures
import {Service} from "@src/com/simple/boot/decorators/ServiceDecorator";
import {Injector} from "@src/com/simple/boot/Injector";
import {map} from "rxjs/operators";

@Service()
export class Foo {
    public name = 'aaa';
}

@Service()
export class Bar {
}

@Service()
export class Foobar {
    constructor(public foo: Foo, public bar: Bar) {
    }
}

@Service()
export class Baz {
    public tag = '';
    constructor(public foobar: Foobar) {
    }
}


class WOW {
    constructor(name: string) {
    }
}

// let newVar = Reflect.getMetadata('design:paramtypes', WOW) || [];
let newVar = Injector.resolve<WOW>(WOW);
console.log('->',newVar);


let foo = Injector.resolve<Baz>(Baz);
console.log('----------', foo)
foo.tag ='aaa'
let foo2 = Injector.resolve<Baz>(Baz);
console.log(foo,'----------', foo2)

// var template = require("./home.hbs")
// // // import template from 'handlebars-loader!./home.hbs';
// let numbers = [1,2,3,4,5,6,7];
// console.log(template({numbers}));
// const {range, fromEvent, interval, Observable, of, Subscription, timer} = require('rxjs');
// const {ajax}  = require('rxjs/ajax')
// ajax.get("http://localhost:8080/hello").subscribe((it: any) => {
//     console.log('ajaxd--> ',it);
// })
// const {map, filter, catchError} = require('rxjs/operators');

// var a = require('html-loader!./home.html');
// console.log('---', a.content);
// console.log(html);
// import html from "html!./home.html";
// import html from "home.html!text";
// var html = require('home.html').default
// import html from 'home.html';
// console.log(html)
// var module = require('index.html')
// import html from 'home.html'
// console.log(html);

// import styles from './home.css';
// import styles from 'css-loader?./home.css';
// console.log(styles)





// var Handlebars = require('handlebars');
// Handlebars.registerHelper('greet', (name:any) => {
//     return 'Hello, name=' + name + '!';
// });


// import 'handlebars/dist/handlebars.min.js';
// import * as Handlebars from "handlebars";
// var source = "<p>Hello, my name is {{name}}. I am from {{hometown}}. I have " +
//     "{{kids.length}} kids:</p>" +
//     "<ul>{{#kids}}<li>{{name}} is {{age}}</li>{{/kids}}</ul>";
// var zz = Handlebars.compile(source);
// //
const Handlebars = require("handlebars");
const template = Handlebars.compile("Name: {{name}}");
console.log('-------------',template({ name: "Nils" }));

// import handlebars from 'handlebars';
// console.log(handlebars);

let numbers = [1,2,3,4,5,6,7];
import home from './home.html';
// console.log(home, Handlebars.compile(home)({numbers}))

document.querySelector("#app")!.innerHTML = Handlebars.compile(home)({numbers});


// var template = require("./home.html")
import templatess from './home.hbs';
console.log('----41414-----',templatess);
// // // import template from 'handlebars-loader!./home.hbs';
// let numbers = [1,2,3,4,5,6,7];
console.log(templatess({numbers}));
// import hbs from "handlebars-inline-precompile";

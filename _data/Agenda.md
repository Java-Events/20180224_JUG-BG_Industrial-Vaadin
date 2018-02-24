<center>
<a href="https://vaadin.com">
 <img src="https://vaadin.com/images/hero-reindeer.svg" width="200" height="200" /></a>
</center>


# Agenda - industrial Vaadin
This is an example agenda, which could be used for a one day
training.
It will cover Vaadin 8 with a Preview on Vaadin 10. (2018.02.21)


# Hello world

We will create a simple Hello World App
and let it run in different Servlet-Containers.

* HelloWorld with Jetty
* HelloWorld with WildFlySwarm
* HelloWorld with Undertow Embedded

Additionally - only to show how it will work?
* HelloWorld with Undertow Embedded - DI
    * how to bootstrap DI
    * MyUIComponentFactory - DDI Servlet


## Core -Elements / -Topics
* Button , TextField, Label
* Binder , Pojo´s
* Navigator : https://vaadin.com/docs/v8/framework/advanced/advanced-navigator.html
* attach() / detach() / Register
* access ui from other thread - ui.access(new Runnable(){}) 
* Handling Errors : https://vaadin.com/docs/v8/framework/application/application-errors.html
* StreamRessources -> ImageCache with MapDB
* @Push - Messages back to Browser
* Composite
* i18n - PropertyService
* grid basics

(optional)
* frp with core java
    * Result
    * Case
    * Memoizing
    * CheckedFunctions
* printing pdf : https://vaadin.com/docs/v8/framework/advanced/advanced-printing.html

### Demo Version 00 - Create it yourself
 * Create the app by yourself
 * store this for later
   
Break - Food - Sleep   
   

### Testing Basics
* Core JUnit5
    * Display Name
    * Disable a test
    * Callbacks
    * Test / Testtemplate
 
* Basics of WebDriver Binaries / maven plugin   
* Selenium - HelloWorld 
* Testbench - HelloWorld
    * TestbenchTestCase
    * Addressing Elements
    * Browser 

#### Testing Demo Version 01
#### Testing Demo Version 00 - self created

#### Testing Best Practices
* IDGenerator
* PageObjects
* TestUI for Components
* Preview for components aka jRebel

##### Mutation Testing
* Core Concept
* Example in Core Java (exercises)
* How to use with Vaadin (Exercises)
* SpeedUp the development


// Mutation Testing - who is able to get the best coverage?
// first write tests against it, as best as possible (30min)
module( "Test portal.events.AppEvents" );
var appEvents = portal.events.AppEvents;    // new 

test( "Test init", function() {
    ok(appEvents != undefined);
});

test( "Test _combineArgs 1", function() {
    var left={a:1,b:2};
    var right={c:3,d:4};
    
    var comb = appEvents._combineArgs(left,right);
    equal(Object.size(comb), 4);
    equal(comb.a, 1);
    equal(comb.d, 4);
});

test( "Test _combineArgs 2", function() {
    var left={a:1,b:2};
    var right={};
    
    var comb = appEvents._combineArgs(left,right);
    equal(Object.size(comb), 2);
    equal(comb.a, 1);
    equal(comb.d, undefined);
});

test( "Test _combineArgs 3", function() {
    var left={a:1,b:2};
    var right=7;
    
    var comb = appEvents._combineArgs(left,right);
    equal(Object.size(comb), 3);
    equal(comb.a, 1);
    equal(comb.other, 7);
});

test( "Test _combineArgs 4", function() {
    var left='a';
    var right=7;
    
    try {
        var comb = appEvents._combineArgs(left,right);
    } catch (e) {
        console.log("Exception: ",e);
        ok(true,"Expected the exception");
        return;
    }
    notOk(true,"Should not reach here as exception expected");
});

test( "Test _combineArgs 5", function() {
    var left=undefined;
    var right=undefined;
    
    var comb = appEvents._combineArgs(left,right);
    console.log("Comb is: ",comb);
    notEqual(comb, undefined);
    equal(Object.size(comb),0);
});

test( "Test _combineArgs 6", function() {
    var left=9;
    var right=undefined;
    
    var comb = appEvents._combineArgs(left,right);
    console.log("Comb is: ",comb);
    notEqual(comb, undefined);
    equal(Object.size(comb),1);
    equal(comb.other, 9);
});

test( "Test broadcast without listeners isnt error", function() {
    AppEvents.broadcast('event');
    ok(true,"broadcasted without listeners");
});

test( "Test addListener and Broadcast", function() {
    AppEvents.clear();
    var myobj = Ext.create('Ext.Component', {
        listeners : {
            event: function(){
                console.log("Event");
                ok(true,"wow!");
            }
        }
    });
    AppEvents.addListener(myobj);
    AppEvents.broadcast('event');
});

test( "Test addListener 2 - check have listeners", function() {
    AppEvents.clear();
    var myObj = Ext.create('Ext.Component', {
        listeners : {
            event: function(){
                console.log("Event");
                ok(true,"wow!");
            }
        }
    });
    var myOtherObj = Ext.create('Ext.Component', {
        listeners : {
            event: function(){
                console.log("Event");
                ok(true,"wow!");
            }
        }
    });
    equal(Object.size(AppEvents.getListeners()), 0);
    AppEvents.addListener(myObj);
    var theListeners = AppEvents.getListeners(); 
    equal(Object.size(theListeners), 1);
    // Same one shouldn't be re-added
    AppEvents.addListener(myObj);
    equal(Object.size(AppEvents.getListeners()), 1);
    // But a new one should be added
    AppEvents.addListener(myOtherObj);
    equal(Object.size(AppEvents.getListeners()), 2);
});

test( "Test addListener 2 - test remove", function() {
    AppEvents.clear();
    var myObj = Ext.create('Ext.Component', {
        listeners : {
            event: function(){
                console.log("Event");
                ok(true,"wow!");
            }
        }
    });
    var myOtherObj = Ext.create('Ext.Component', {
        listeners : {
            event: function(){
                console.log("Event");
                ok(true,"wow!");
            }
        }
    });
    equal(Object.size(AppEvents.getListeners()), 0);
    AppEvents.addListener(myObj);
    AppEvents.addListener(myOtherObj);
    equal(Object.size(AppEvents.getListeners()), 2);
    AppEvents.removeListener(myOtherObj);
    equal(Object.size(AppEvents.getListeners()), 1);
    AppEvents.removeListener(myObj);
    equal(Object.size(AppEvents.getListeners()), 0);
});

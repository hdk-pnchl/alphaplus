var serviceM= angular.module('servicesM', ['ngResource']);

serviceM.factory('alphaplusService', function($resource){
    var webResource= {};
    
    //webResource.rootPath= 'http://104.238.126.194:8080/alphaplus';
    webResource.rootPath= 'http://localhost:8080/alphaplus';

    webResource.core= $resource(webResource.rootPath+'/ctrl/core/:action',{
        action: '@action'
    });
    webResource.message= $resource(webResource.rootPath+'/ctrl/message/:action',{
        action: '@action'
    });
    webResource.job= $resource(webResource.rootPath+'/ctrl/job/:action',{
        action: '@action'
    });
    webResource.client= $resource(webResource.rootPath+'/ctrl/client/:action',{
        action: '@action'
    });  

    // User
    webResource.user= $resource(webResource.rootPath+'/ctrl/user/:action',{
        action: '@action'
    });    
    webResource.address= $resource(webResource.rootPath+'/ctrl/user/address/:action',{
        action: '@action'
    });  
    webResource.basicDetail= $resource(webResource.rootPath+'/ctrl/user/basicDetail/:action',{
        action: '@action'
    });  

   

    return webResource;
});

serviceM.factory('alphaplusGlobleDataService', function($resource){
    var alphaplusGlobleDataService= {};
    return alphaplusGlobleDataService;
});
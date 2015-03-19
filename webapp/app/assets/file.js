/**
 * Created by chota_don on 03-02.
 */
var myApp=angular.module('myApp',[]);
function MyCtrl($scope,$http){
    //var r='http://'+$scope.url;
    $scope.imageUpload=function(element){
        var reader= new FileReader();
        reader.onload=$scope.imageIsLoaded;
        reader.readAsDataURL(element.files[0]);
    }
    $scope.imageIsLoaded=function(e){
        $scope.$apply(function(){
            $scope.data=e.target.result;
        });
    }

    $scope.show=function(){
        var url="localhost:9000"
        var urlText=
        {
            "url":$scope.url
        };
        $http({
            method:"GET",
            url:url+"/getUrl",
            params:{
                data:urlText
            }
        })
            .success(function(data,status,headers,config){
                $scope.url-title.push(data.title)
                $scope.url-description.push(data.description)
                $scope.url-img.push(data.img)
            })

    }


}


myApp.directive('ngEnter', function() {
    return function(scope, element, attrs) {
        element.bind("keydown keypress", function(event) {
            if(event.which === 13) {
                scope.$apply(function(){
                    scope.$eval(attrs.ngEnter);
                });

                event.preventDefault();

            }
        });


    };
});

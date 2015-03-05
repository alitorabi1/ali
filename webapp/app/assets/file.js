/**
 * Created by chota_don on 15-03-02.
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
    $scope.call=function(){

        $http.get('www.socialorra.com').
            success(function(data,status,headers,config){
                $scope.url=$(data).filter('meta[name=title]').attr("content");
            }).
            error(function(data,status,headers,config){

            });

    }

    // $scope.pre=function(){
// 		angular.element("preview").display("block");
// 	}
}




// var myapp=angular.module('myapp',[]);
// function MyCtrl($scope){
// 	$scope.add=function(){
// 		var f=document.getElementById('file').files[0],
// 		r= new FileReader();
// 		r.onloadend=function(e){
// 			$scope.data=e.target.result;
// 		}
// 		r.readAsBinaryString(f);
// 	}
// }
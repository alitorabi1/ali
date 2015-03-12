'use strict';

requirejs.config({
	paths: {
		'angular': ['../lib/angularjs/angular']
	},
	shim: {
		'angular': {
			exports: 'angular'
		}
	}
});

require(['angular'], function(angular) {
	var so;
	so = angular.module('so', []);
	return angular.bootstrap(document, ['so']);
});

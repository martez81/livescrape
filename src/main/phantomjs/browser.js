"use strict";

/*
 * ex1. browser.js "https://www.google.com/finance?cid=5768664" "div.snap-panel-and-plusone,div.snap-panel,tr[2],td[1]"
 * ex2. browser.js "https://www.google.com/finance?cid=5768664" "span#ref_5768664_l,span[0]"
 * ex3. ~/Downloads/phantomjs-2.1.1-macosx/bin/phantomjs --disk-cache=true browser.js "http://www.realclearpolitics.com/epolls/latest_polls/" "div#table-1,table@1,tbody,tr,td@2"
 *
 */

var system = require('system');

if (system.args.length != 3) {
    console.log('Requires 2 arguments - url and html target location string.');
    phantom.exit();
}

var url = system.args[1];
var webPage = require('webpage');
var page = webPage.create();

//page.onConsoleMessage = function(msg) {
//    console.log(msg);
//};

page.open(url, function(status) {
    page.injectJs('jquery-2.2.4.min.js');

    var target_string = system.args[2];
    var xs = target_string.split(',');

//    console.log('URL: ' + url);
//    console.log('Locator String: ' + target_string);

    function resolveNew(xs) {
        var res1 = page.evaluate(function(xs) {
            function parseElementIndex(s) {
                if (typeof(s) === 'undefined') {
                    console.log('Due to undefined, returning 0.');
                    return 0;
                } else if (s.indexOf("@") > 0) {
                    var idx = s.split("@")[1];
                    console.log('Found index: ' + idx + '.');
                    return idx
                } else {
                    console.log('No match, returning 0.');
                    return 0;
                }
            }

            function parseAny(s) {
                if (s.indexOf("*") > 0) {
                    return true;
                }
                return false;
            }

            function recursive(xs, el) {
                if (xs.length > 0) {
                    var x = xs[0];
                    var idx = parseElementIndex(x);
                    var any = parseAny(x);

                    var tag = '';
                    if (x.indexOf("@") > 0) {
                        tag = x.split("@")[0];
                    } else if (x.indexOf("*") > 0) {
                        tag = x.split("*")[0];
                    } else {
                        tag = x;
                    }

                    if (el.length > 1) {
                        var temp = [];
                        for (i = 0; i < el.length; i++) {
                            if (any) {
                                var tempRes = $(el[i]).find(tag);
                                if (tempRes.length > 1) {
                                    return recursive(xs.slice(1), tempRes);
                                }
                            } else {
                                var tempRes = $(el[i]).find(tag).eq(idx);
                            }

                            temp.push(tempRes);
                        }
                        return recursive(xs.slice(1), temp);
                    } else {
                        if (any) {
                            return recursive(xs.slice(1), $(el[0]).find(tag));
                        } else {
                            return recursive(xs.slice(1), [$(el[0]).find(tag).eq(idx)]);
                        }
                    }
                }

                return el;
            }

            var x = xs[0];
            var idx = parseElementIndex(xs[0]);
            var any = parseAny(xs[0]);

            var res = recursive(xs.slice(1), [$(x).eq(idx)]);
            var resp = [];

            for (i = 0; i < res.length; i++) {
                resp.push($(res[i]).text());
            }

            return resp;

//            var res = $(xs[0]).eq(0);
//            var x = xs[0];
//            var idx = parseElementIndex(xs[0]);
//
//            x = x.split("@")[0];
//
//            for (i = 1; i < xs.length; i++) {
//                idx = parseElementIndex(xs[i]);
//                x = xs[i];
//
//                if (x.split("@").length > 1) {
//                    x = x.split("@")[0];
//                }
//
//                res = $(res).find(x).eq(idx);
//            }
//
//            return res.text();

        }, xs);
        return res1;
    }

    var data = resolveNew(xs);
    console.log(JSON.stringify({url: url, target: target_string, value: data}));
    phantom.exit();
});

var webPage = require('webpage');
var page = webPage.create();

page.open('https://www.google.com/finance?q=gdxj&ei=L6lsV9iDEZHAev7etdgL', function (status) {
  var url = page.url;
  console.log('URL: ' + url);
  page.injectJs('jquery-2.2.4.min.js');

  var value = page.evaluate(function() {
    return $('#ref_5768664_l').html();
  });
  console.log('value: ' + value);

  phantom.exit();
});


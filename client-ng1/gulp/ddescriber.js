var through = require("through2");

module.exports = function ddescriber(options) {

  return through.obj(function (file, enc, cb) {
    var contents = file.contents.toString();
    var err = null;

    if (/.*ddescribe|iit|fdescribe|fit/.test(contents)) {
      err = new Error('\033[31mddescribe, fdescribe, iit or fit present in file ' + file.path + '\033[0m');
    }
    cb(err, file);
  });
};

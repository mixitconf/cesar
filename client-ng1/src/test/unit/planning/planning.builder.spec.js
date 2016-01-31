var builders = (function () {
  'use strict';

  //Object Slot
  function Slot(id) {
    this.slot = {
      "id" : id
    };
  }
  Slot.prototype.room = function (room) {
    this.slot.room = title;
    return this;
  };
  Slot.prototype.session = function (session) {
    this.slot.session = session;
    return this;
  };
  Slot.prototype.range = function (start, end) {
    this.slot.start = start;
    this.slot.end = end;
    return this;
  };
  Slot.prototype.label = function (label) {
    this.slot.label = label;
    return this;
  };
  Slot.prototype.build = function () {
    return this.session;
  };

  //Object Session
  function Session(id) {
    this.session = {
      "id": id,
      "format": "Talk"
    };
  }
  Session.prototype.title = function (title) {
    this.session.title = title;
    return this;
  };
  Session.prototype.lang = function (lang) {
    this.session.lang=lang;
    return this;
  };
  Session.prototype.build = function () {
    return this.session;
  };

  return {
    createSession : function(id){
      return new Session(id);
    },
    createSlot : function(id){
      return new Slot(id);
    }
  }
})();


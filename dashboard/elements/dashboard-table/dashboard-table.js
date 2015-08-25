Polymer({
  is: 'dashboard-table',
  properties: {
    count: {
      type: Number,
      value: 0,
      notify: true
    }
  },
  listeners: {
    'checkAll.tap': 'checkAllTap'
  },
  ready: function() {
    return this.addEventListener('iron-change', (function(_this) {
      return function() {
        var checkbox, i, len, ref, results;
        _this.count = 0;
        ref = _this.getElementsByTagName('paper-checkbox');
        results = [];
        for (i = 0, len = ref.length; i < len; i++) {
          checkbox = ref[i];
          if (checkbox.checked === true && checkbox.id !== 'checkAll') {
            results.push(_this.set('count', _this.count + 1));
          } else {
            results.push(void 0);
          }
        }
        return results;
      };
    })(this));
  },
  checkAllTap: function(event) {
    var checkbox, i, len, ref, results;
    ref = this.getElementsByTagName('paper-checkbox');
    results = [];
    for (i = 0, len = ref.length; i < len; i++) {
      checkbox = ref[i];
      results.push(checkbox.checked = event.currentTarget.checked);
    }
    return results;
  },
  deleteSelection: function() {
    return console.log('log');
  }
});

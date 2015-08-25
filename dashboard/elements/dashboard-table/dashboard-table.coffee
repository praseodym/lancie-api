Polymer
  is: 'dashboard-table'

  properties:
    count:
      type: Number
      value: 0
      notify: true

  listeners:
    'checkAll.tap': 'checkAllTap'

  ready: ->
    this.addEventListener 'iron-change', =>
      @count = 0
      for checkbox in @getElementsByTagName 'paper-checkbox'
        if checkbox.checked is true and checkbox.id isnt 'checkAll'
          @.set 'count', @count + 1

  checkAllTap: (event) ->
    checkbox.checked = event.currentTarget.checked for checkbox in @getElementsByTagName 'paper-checkbox'

  deleteSelection: ->
    console.log 'log'
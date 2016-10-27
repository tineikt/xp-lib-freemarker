# lib-freemarker
Attempt at rendering Freemarker with Enonic XP

## Installation

...

## Usage
Just as you are used to with Thymeleaf in your controller

```javascript
var freemarker = require('/lib/xp/freemarker');

exports.get = function(req) {
  var model = {

  }

  var view = resolve('template.ftl');
  var html = freemarker.render(view, model);

  return {
    body: html
  }
};
```

### View Functions

[Enonic View Functions documentation](http://xp.readthedocs.io/en/stable/reference/view-functions/index.html)

Example usage in Thymeleaf:
```html
<a data-th-href="${portal.assetUrl({'_path=css/main.css'})}">Link</a>
<img data-th-src="${portal.imageUrl({'_id=869b29a0-dccc-4d5e-afc5-81e5050a628d', '_scale=block(50,50)'})}"/>
```

Example usage in Freemarker
```html
<a href=<@assetUrl path="css/main.css"/>>Link</a>
<img src=<@imageUrl scale="block(50,50)" id="869b29a0-dccc-4d5e-afc5-81e5050a628d"/>>
```

### Component reference

Enonic XP does some post-processing to retrieve and render components.
You are probably used to Thymeleaf and how some magic happens when you use the `data-portal-component`.

```html
<div data-th-each="component : ${regions.components}" data-th-remove="tag">
  <div data-portal-component="${component.path}" data-th-remove="tag" />
</div>
```
This magic is implemented using a Freemarker directive, `<@component path=component.path />`
The same template, in Freemarker would then be.
```html
<#list regions.components as component>
  <@component path=component.path />
</#list>```

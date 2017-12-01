# <img src="https://webfiles.tine.no/Logo/TINE-logo.svg" alt="Drawing" style="width: 40px;"/> lib-freemarker
Attempt at rendering Freemarker with Enonic XP

## Installation

The most straight forward way to get it going is probably jitpack, which builds and makes a public github repo available as a gradle dependency on-the-fly:

Step 1: Add it in your root build.gradle at the end of repositories:
```javascript
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

Step 2. Add the dependency
```javascript
dependencies {
  include 'com.github.tineikt:xp-lib-freemarker:1.1.0'
}
```

Hopefully this should allow jitpack to build the project, and present it as a standard gradle package for Enonic XP :)

## Usage
Just as you are used to with Thymeleaf in your controller

```javascript
var freemarker = require('/lib/tineikt/freemarker');

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
<p data-th-text="${portal.localize({'_key=mystring','_locale=en'})}">Not translated</p>
```

Example usage in Freemarker
```html
<a href="[@assetUrl path='css/main.css'/]">Link</a>
<img src="[@imageUrl scale='block(50,50)' id='869b29a0-dccc-4d5e-afc5-81e5050a628d'/]">
<p>[@localize locale='en' key='mystring'/]</p>

```

### Freemarker documentation

[What is Apache FreeMarker™?](https://freemarker.apache.org/)

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
[#list regions.components as component]
  [@component path=component.path /]
[/#list]```

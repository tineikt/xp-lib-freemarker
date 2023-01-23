# <a href="https://www.tine.no/"><img src="https://webfiles.tine.no/Logo/TINE-logo.svg" alt="TINE Logo" width="70" align="right"></a> lib-freemarker
Freemarker library for Enonic XP

## Installation

The most straight forward way to get it going is probably jitpack, which builds and makes a public github repo available as a gradle dependency on-the-fly:

Step 1: Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

Step 2. Add the dependency
If you are on an Enonic XP 6.15 add the dependency like this:
```groovy
dependencies {
  include 'com.github.tineikt:xp-lib-freemarker:1.1.1'
}
```
If you are on an Enonic XP 7 add the dependency like this:
```groovy
dependencies {
  include 'no.tine.xp:xp-lib-freemarker:2.0.2'
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
```ftl
<a href="[@assetUrl path='css/main.css'/]">Link</a>
<img src="[@imageUrl scale='block(50,50)' id='869b29a0-dccc-4d5e-afc5-81e5050a628d'/]">
<p>[@localize locale='en' key='mystring'/]</p>
```

#### Fixing unresolved references in IntelliJ

If you are using one of the IDEs from Jetbrains, a [special comment syntax](https://www.jetbrains.com/help/idea/template-data-languages.html#special-comments) 
exists that will fix unresolved references to *Enonic View Functions* in your project.

You can simply add a file "*./src/main/resources/freemarker_implicit.ftl*" with the following contents to your project.

```ftl
[#ftl]
[#-- @implicitly included --]

[#macro pageUrl id="" path="" type="server"][/#macro]
[#macro assetUrl path application="" type="server"][/#macro]
[#macro imageUrl scale id="" path="" format="" quality=85 background="" filter="" type="server"][/#macro]
[#macro attachmentUrl id="" path="" name="" label="source" download=false type="server"][/#macro]
[#macro componentUrl id="" path="" component="" type="server"][/#macro]
[#macro serviceUrl service application="" type="server"][/#macro]
[#macro localize key locale=""][/#macro]
[#macro processHtml value type="server"][/#macro]
[#macro imagePlaceholder width height][/#macro]
```

> **Note:**  
> **Protip**: You can provide type checking to your Freemarker-templates by creating `@ftlvariable`
> comments on the top of your ftl-files.
> 
> ```ftl
> [#-- @ftlvariable name="displayName" type="java.lang.String" --]
> <h1>${displayName}</h1>
> ```

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

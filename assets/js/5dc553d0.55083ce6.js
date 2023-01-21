"use strict";(self.webpackChunkdocs=self.webpackChunkdocs||[]).push([[219],{3905:(e,t,i)=>{i.d(t,{Zo:()=>s,kt:()=>f});var n=i(7294);function r(e,t,i){return t in e?Object.defineProperty(e,t,{value:i,enumerable:!0,configurable:!0,writable:!0}):e[t]=i,e}function a(e,t){var i=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),i.push.apply(i,n)}return i}function o(e){for(var t=1;t<arguments.length;t++){var i=null!=arguments[t]?arguments[t]:{};t%2?a(Object(i),!0).forEach((function(t){r(e,t,i[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(i)):a(Object(i)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(i,t))}))}return e}function l(e,t){if(null==e)return{};var i,n,r=function(e,t){if(null==e)return{};var i,n,r={},a=Object.keys(e);for(n=0;n<a.length;n++)i=a[n],t.indexOf(i)>=0||(r[i]=e[i]);return r}(e,t);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);for(n=0;n<a.length;n++)i=a[n],t.indexOf(i)>=0||Object.prototype.propertyIsEnumerable.call(e,i)&&(r[i]=e[i])}return r}var c=n.createContext({}),p=function(e){var t=n.useContext(c),i=t;return e&&(i="function"==typeof e?e(t):o(o({},t),e)),i},s=function(e){var t=p(e.components);return n.createElement(c.Provider,{value:t},e.children)},d="mdxType",u={inlineCode:"code",wrapper:function(e){var t=e.children;return n.createElement(n.Fragment,{},t)}},m=n.forwardRef((function(e,t){var i=e.components,r=e.mdxType,a=e.originalType,c=e.parentName,s=l(e,["components","mdxType","originalType","parentName"]),d=p(i),m=r,f=d["".concat(c,".").concat(m)]||d[m]||u[m]||a;return i?n.createElement(f,o(o({ref:t},s),{},{components:i})):n.createElement(f,o({ref:t},s))}));function f(e,t){var i=arguments,r=t&&t.mdxType;if("string"==typeof e||r){var a=i.length,o=new Array(a);o[0]=m;var l={};for(var c in t)hasOwnProperty.call(t,c)&&(l[c]=t[c]);l.originalType=e,l[d]="string"==typeof e?e:r,o[1]=l;for(var p=2;p<a;p++)o[p]=i[p];return n.createElement.apply(null,o)}return n.createElement.apply(null,i)}m.displayName="MDXCreateElement"},4440:(e,t,i)=>{i.r(t),i.d(t,{assets:()=>c,contentTitle:()=>o,default:()=>d,frontMatter:()=>a,metadata:()=>l,toc:()=>p});var n=i(7462),r=(i(7294),i(3905));const a={sidebar_position:3,title:"initialize"},o="initialize",l={unversionedId:"api/initialize",id:"api/initialize",title:"initialize",description:"Initializes the health connect client with specific providers. If providerPackageNames is not provided, the default Health Connect application package name will be considered com.google.android.apps.healthdata.",source:"@site/docs/api/initialize.md",sourceDirName:"api",slug:"/api/initialize",permalink:"/react-native-health-connect/docs/api/initialize",draft:!1,editUrl:"https://github.com/matinzd/react-native-health-connect/tree/main/docs/docs/api/initialize.md",tags:[],version:"current",sidebarPosition:3,frontMatter:{sidebar_position:3,title:"initialize"},sidebar:"tutorialSidebar",previous:{title:"isAvailable",permalink:"/react-native-health-connect/docs/api/isAvailable"},next:{title:"requestPermission",permalink:"/react-native-health-connect/docs/api/requestPermission"}},c={},p=[],s={toc:p};function d(e){let{components:t,...i}=e;return(0,r.kt)("wrapper",(0,n.Z)({},s,i,{components:t,mdxType:"MDXLayout"}),(0,r.kt)("h1",{id:"initialize"},(0,r.kt)("inlineCode",{parentName:"h1"},"initialize")),(0,r.kt)("p",null,"Initializes the health connect client with specific providers. If ",(0,r.kt)("inlineCode",{parentName:"p"},"providerPackageNames")," is not provided, the default Health Connect application package name will be considered ",(0,r.kt)("inlineCode",{parentName:"p"},"com.google.android.apps.healthdata"),"."),(0,r.kt)("h1",{id:"method"},"Method"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-ts"},"initialize(providerPackageNames: string[] = [DEFAULT_PROVIDER_PACKAGE_NAME]): Promise<boolean>;\n")),(0,r.kt)("h1",{id:"example"},"Example"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-ts"},"import { initialize } from 'react-native-health-connect';\n\nconst onInitialize = async () => {\n  const isInitialized = await initialize();\n  console.log({ isInitialized });\n};\n")))}d.isMDXComponent=!0}}]);
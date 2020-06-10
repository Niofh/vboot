
* [springboot搭建多模块](https://blog.csdn.net/qq_37604508/article/details/83047198)
* 子模块如果是pom是不需要引入的，而是引入子模块中子模块
  * vboot-common 是pom。这里引入一些核心工具，子模块也可以用
  * vboot-file 是jar,是子模块，引入的时候引入子模块
  
  

* [springboot多模块](https://blog.csdn.net/weixin_33347597/article/details/81039153)


### git代码规范
[git代码规范](https://blog.csdn.net/ligang2585116/article/details/80284819)

feat：新增功能；
fix：修复bug；
docs：修改文档；
refactor：代码重构，未新增任何功能和修复任何bug；
build：改变构建流程，新增依赖库、工具等（例如webpack修改）；
style：仅仅修改了空格、缩进等，不改变代码逻辑；
perf：改善性能和体现的修改；
chore：非src和test的修改；
test：测试用例的修改；
ci：自动化流程配置修改；
revert：回滚到上一个版本；

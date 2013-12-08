//去除了FastJson的固定默认引用，要调用者自己指定数据转为Java的东西
增加Model，由于在一个页面可能提交多次不同数据，
new Model(Object)---他是不通过get远程服务器数据，通过用户指定的数据来提交，
            也就是会自动把obj数据转为RequestParams，然后进行提交
new Model(Object container,Object obj)他会从container注入以及获取数据


如果返回值才是我们需要的，请使用get(会自动把返回值转为Model，在把Model注入到VIew中)
如果返回值不是我们主要目的，主要目的是把数据上传到服务器，请使用post（会自动把View的数据注入到Model中，在把Model的数据传到服务器）
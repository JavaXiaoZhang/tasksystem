(function($){
    $.fn.moduleConfig = function(method) {
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(
                arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist on jQuery.moduleConfig');
        }
    };
    $.fn.moduleAdd = function(method) {
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(
                arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return methods.initModuleAdd.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist on jQuery.moduleAdd');
        }
    };
    $.fn.moduleDelete = function(method) {
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(
                arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return methods.initModuleDelete.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist on jQuery.moduleDelete');
        }
    };
    $.fn.moduleConfig._default = {

    };
    $.fn.moduleAdd._default = {

    };
    var methods = {
        _options : null,
        _targetDom : null,
        _moveArea : null,
        _areaWidth : null,
        _areaHeight : null,
        _singleWidth : null,
        _singleHeight : null,
        _xLen : null,
        _yLen : null,
        _default_zIndex : 8,
        _containerArr : [],
        init : function(_options){
            _options = $.extend($.fn.moduleConfig._default,_options);
            methodsDefault._options = _options;
            methodsDefault._initMoveArea();
        },
        initModuleAdd : function(_addOptions){
            _addOptions = $.extend($.fn.moduleAdd._default,_addOptions);
            var _xLen = _addOptions.size.split(",")[0];
            var _yLen = _addOptions.size.split(",")[1];
            if(!_addOptions.positionX){
                _addOptions.positionX = 0;
            }
            if(!_addOptions.positionY){
                _addOptions.positionY = 0;
            }
            var _positionTop = methodsDefault._singleHeight*_addOptions.positionX;
            var _positionLeft = methodsDefault._singleWidth*_addOptions.positionY;
            var html = '<div module-target="module" id="'+_addOptions.id+'" module-key="'+_addOptions.databaseId+'" module-size="'+_addOptions.size+'" style="cursor:move;position:absolute;top:'+_positionTop+'px;left:'+_positionLeft+'px;width:'+methodsDefault._singleWidth*_yLen+'px;height:'+methodsDefault._singleHeight*_xLen+'px;z-index:'+(methodsDefault._default_zInde+1)+';">';
            html += _addOptions.html;
            html += '</div>';
            methodsDefault._moveArea.append(html);
            methodsDefault._targetDom = $("div[module-target='module']");
            methodsDefault._initMoveDivCss();
            methodsDefault._initTargetDivMoveEvent();
            //methodsDefault._resetModuleContainer();
            if(_addOptions.callback){
                _addOptions.callback(_addOptions,function(moduleKey){
                    $("#"+_addOptions.id).attr("module-key",moduleKey);
                });
            }
        },
        initModuleDelete : function(_delOptions){
            var _deleteDiv = $("#"+_delOptions.id);
            var moduleKey = _deleteDiv.attr("module-key");
            var _positionX = _deleteDiv.position().top;
            var _positionY = _deleteDiv.position().left;
            var _containerX = Math.round(_positionX/methodsDefault._singleHeight);
            var _containerY = Math.round(_positionY/methodsDefault._singleWidth);
            $("#"+_delOptions.id).remove();
            //methodsDefault._resetModuleContainer();
            if(_delOptions.callback){
                _delOptions.callback(moduleKey);
            }
        },
        _initMoveArea : function(){
            var _options = methodsDefault._options;
            methodsDefault._moveArea = _options.moveArea;
            methodsDefault._areaWidth = methodsDefault._moveArea.width();
            methodsDefault._areaHeight = methodsDefault._moveArea.height();
            methodsDefault._xLen = _options.x*1;
            methodsDefault._yLen = _options.y*1;
            methodsDefault._singleWidth = Math.floor(methodsDefault._areaWidth/methodsDefault._yLen);
            methodsDefault._singleHeight = Math.floor(methodsDefault._areaHeight/methodsDefault._xLen);
            methodsDefault._initContainerArr();
            methodsDefault._initModuleGridLines();
            if(_options.callbackSingleSize){
                _options.callbackSingleSize(methodsDefault._singleWidth,methodsDefault._singleHeight);
            }
        },
        _initMoveDivCss : function(){
            var _targetDom = methodsDefault._targetDom;
            methodsDefault._targetDom.each(function(){
                var size = $(this).attr("module-size");
                $(this).width(size.split(",")[1]*1*methodsDefault._singleWidth-2).height(size.split(",")[0]*1*methodsDefault._singleHeight-2);
            });
        },
        _initTargetDivMoveEvent : function(){
            var _origionX = "";
            var _origionY = "";
            methodsDefault._targetDom.each(function(){
                var _moveDiv = $(this);
                var mouseMoveFlag = false;
                _moveDiv.unbind('mousedown').bind('mousedown',function(event) {
                    if(event.target.tagName!="INPUT"){//input标签会使元素失去mouseup事件
                        _origionX = _moveDiv.position().left;
                        _origionY = _moveDiv.position().top;
                        var e = event || window.event;
                        _moveDiv.css({"cursor":"move","z-index":"20"});
                        var x = e.pageX - _moveDiv.position().left;
                        var y = e.pageY - _moveDiv.position().top;
                        methodsDefault._moveArea.unbind('mousemove').bind('mousemove', function(event) {
                            mouseMoveFlag = true;
                            _moveDiv.css({"cursor":"move","z-index":"30"});
                            var ev = event || window.event;
                            $("div[module-target='grid-line']").show();
                            var abs_x = ev.pageX - x;
                            var abs_y = ev.pageY - y;
                            if(abs_x < 0){
                                abs_x = 0;
                            }else if(abs_x > methodsDefault._moveArea.width()- _moveDiv.width()){
                                abs_x = methodsDefault._moveArea.width()- _moveDiv.width();
                            }
                            if(abs_y < 0){
                                abs_y = 0;
                            }else if(abs_y > methodsDefault._moveArea.height() - _moveDiv.height()){
                                abs_y = methodsDefault._moveArea.height() - _moveDiv.height();
                            }
                            _moveDiv.css({'left': abs_x, 'top': abs_y});
                        })
                    }
                });
                _moveDiv.unbind('mouseup').bind('mouseup',function(event) {
                    if(!mouseMoveFlag){  //防止浏览器f12调试查看元素时触发mouseup事件
                        methodsDefault._moveArea.unbind('mousemove');
                        return;
                    }
                    var e = event || window.event;
                    var _positionX = _moveDiv.position().top;
                    var _positionY = _moveDiv.position().left;
                    var _containerX = Math.round(_positionX/methodsDefault._singleHeight);
                    var _containerY = Math.round(_positionY/methodsDefault._singleWidth);
                    _moveDiv.css({'left': _containerY*methodsDefault._singleWidth+2,'top': _containerX*methodsDefault._singleHeight+2});
                    _moveDiv.css({'z-index':'8'});
                    methodsDefault._moveArea.unbind('mousemove');
                    $("div[module-target='grid-line']").hide();
                    //_moveDiv.css("z-index",methodsDefault._default_zIndex + methodsDefault._containerArr[_containerX][_containerY]);
                    //methodsDefault._resetModuleContainer();
                    if(methodsDefault._options.onMouseup){
                        var mouseUpObj = {
                            divName : _moveDiv.attr("id"),
                            id : _moveDiv.attr("module-key"),
                            x : _containerX,
                            y : _containerY,
                            divSize : _moveDiv.attr("module-size")
                        };
                        methodsDefault._options.onMouseup(mouseUpObj);
                    }
                });
            });
        },
        _resetModuleContainer : function(){
            methodsDefault._initContainerArr();
            methodsDefault._targetDom.each(function(){
                var _moveDiv = $(this);
                var _moveDivSize = _moveDiv.attr("module-size");
                var _sizeX = _moveDivSize.split(",")[0];
                var _sizeY = _moveDivSize.split(",")[1];
                var _positionX = _moveDiv.position().top;
                var _positionY = _moveDiv.position().left;
                var _containerX = Math.round(_positionX/methodsDefault._singleHeight);
                var _containerY = Math.round(_positionY/methodsDefault._singleWidth);
                for(var i=0;i<_sizeX;i++){
                    for(var j=0;j<_sizeY;j++){
                        if(methodsDefault._containerArr[_containerX+i][_containerY+j] > 0){
                            methodsDefault._containerArr[_containerX][_containerY]++;
                        }else{
                            methodsDefault._containerArr[_containerX+i][_containerY+j] = 1;
                        }
                    }
                }
            })
        },
        _initModuleGridLines : function(){
            var color = "#4876FF";
            var html  = '<div module-target="grid-line" style="background: rgb(31, 86, 169);display:none;position:absolute;top:0;margin:0px;border-top:2px solid '+color+';border-left:2px solid '+color+';z-index:5;">';
            html += '     <ul style="width:'+(methodsDefault._areaWidth-2)+'px;height:'+(methodsDefault._areaHeight-2)+'px;margin:0px;padding:0px;list-style: none;">';
            for(var i=0;i<methodsDefault._xLen*methodsDefault._yLen;i++){
                html += '     <li style="width:'+((methodsDefault._areaWidth-2)/methodsDefault._yLen-2)+'px;height:'+((methodsDefault._areaHeight-2)/methodsDefault._xLen-2)+'px;float:left;border-right:2px solid '+color+';border-bottom:2px solid '+color+';"></li>';
            }
            html += '     </ul>';
            html += '</div>';
            if($("div[module-target='grid-line']").length==0){
                methodsDefault._moveArea.append(html);
            }
        },
        _initContainerArr : function(){
            for(var i=0;i<methodsDefault._xLen;i++){
                methodsDefault._containerArr[i] = new Array();
                for(var j=0;j<methodsDefault._yLen;j++){
                    methodsDefault._containerArr[i][j] = 0;
                }
            }
        }
    };
    var methodsDefault = methods;
})(jQuery)

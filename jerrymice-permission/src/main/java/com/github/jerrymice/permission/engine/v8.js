/**
 * 判断对象是否为空
 * @param t
 * @returns {boolean}
 */
empty = function (t) {
    if (t == undefined || t == null || t == '') {
        return true;
    }
    if (t instanceof Array) {
        return t.length == 0;
    }
    if (t instanceof Object) {
        return Object.keys(t).length == 0;
    }
    return false;
}
/**
 * 删除属性
 * @param names
 * @returns {Object}
 */
Object.prototype.delete = function (names) {
    if (names instanceof Array) {
        for (var i = 0; i < names.length; i++) {
            var name = names[i];
            if (this.hasOwnProperty(name)) {
                this[names[i]]=null;
            }
        }
    } else {
        this[names]=null;
    }
    return this;
}

/**
 * 遍历对象,同forEach,但支持链式调用.
 * @param callback
 * @param thisArg
 * @returns {*}
 */
Array.prototype.each = function (callback, thisArg) {
    if (thisArg == undefined) {
        thisArg = this;
    }
    thisArg.forEach((v, i, t) => callback(v, i, t), thisArg);
    return thisArg;
}
/**
 * 删除数据中的空对象
 * @returns {Array}
 */
Array.prototype.emptyRemove = function () {
    for (var i = 0; i < this.length; i++) {
        if (Object.keys(this[i]).length == 0 || this[i] == null || this[i] == undefined) {
            this.splice(i, 1);
            i--;
        }
    }
    return this;
}

/**
 * 删除数组元素.会影响自身
 @param {function(T=, number=, Array.<T>=)} callback
 @param {*} [thisArg]
 @return {Array}
 */
Array.prototype.removes = function (callback, thisArg) {
    if (thisArg == undefined) {
        thisArg = this;
    }
    for (var i = 0; i < this.length; i++) {
        if (callback(this[i], i, thisArg)) {
            this.splice(i, 1);
            i--;
        }
    }
    return thisArg;
}
/**
 * 同push,但支持链式调用
 * @param items
 * @returns {Array}
 */
Array.prototype.put = function (items) {
    this.push(items);
    return this;
}
/**
 * 合并数组
 * @param element  可以是一个数组,如果对象是一个数组,那么数组的每个元素将会被添加到源对象
 * @returns {Array} 源对象,
 */
Array.prototype.merge = function (element) {
    for (var t = 0; t < arguments.length; t++) {
        element = arguments[t];
        if (element instanceof Array) {
            for (var i = 0; i < element.length; i++) {
                this.push(element[i]);
            }
        } else {
            this.push(element);
        }
    }
    return this;
}
/**
 * 插入元素
 * @param index
 * @param value
 * @returns {Array}
 */
Array.prototype.insert = function (index, value) {
    this.splice(index, 0, value);
    return this;
}
/**
 * 合并数组元素.会影响自身
 @param {function(source=, target=)} callback 两个参数.source 源数组 ; target 要合并的数据.
 @param {*} [element] 要合并的数组.
 @return {Array}
 */
Array.prototype.merges = function (callback, element) {
    for (var t = 1; t < arguments.length; t++) {
        callback = arguments[0];
        if (arguments.length > 1) {
            element = arguments[t];
        }
        if (typeof callback !== 'function') {
            if (element === undefined || element.length == 0) {
                element = callback
            } else {
                element.insert(0, callback);
            }
            for (var i = 0; i < element.length; i++) {
                this.push(element[i])
            }
        } else {
            if (element !== undefined && element.length > 0) {
                //如果第一个元素是一个函数,那么将调用该函数,是否合并,取决于函数.
                callback(this, element)
            }
        }
    }
    return this;
}
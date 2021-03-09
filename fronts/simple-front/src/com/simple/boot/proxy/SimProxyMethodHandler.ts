export class SimProxyMethodHandler implements ProxyHandler<any> {
    public get(target: any, name: string): any {
        console.log('SimProxyMethodHandler get', target, name)
        return name in target ? target[name] : 37
    }

    public set(obj: any, prop: string, value: any): boolean {
        console.log('SimProxyMethodHandler set', obj, prop, value)
        if (prop === 'age') {
            if (!Number.isInteger(value)) {
                throw new TypeError('The age is not an integer')
            }
            if (value > 200) {
                throw new RangeError('The age seems invalid')
            }
        }

        // The default behavior to store the value
        obj[prop] = value
        return true
    }

    apply(target: any, thisArg: any, argumentsList?: any): any {
        // console.log('SimProxyMethodHandler apply', target, 'thisArg',thisArg, 'arg',argumentsList);
        console.log('SimProxyMethodHandler apply', 'arg', argumentsList);
        return target.apply(thisArg, argumentsList);
        // return argumentsList[0] + argumentsList[1] + argumentsList[2];
    }
}

import {Module} from '@src/com/simple/boot/module/Module'

export class SimProxyMethodHandler implements ProxyHandler<any> {
    public get(target: any, name: string): any {
        // console.log('SimProxyMethodHandler get', target, name)
        return target[name];
        // return name in target ? target[name] : 37
    }

    public set(obj: any, prop: string, value: any): boolean {
        console.log('SimProxyMethodHandler set :', prop, ' value:', value, ' obj:', obj)
        obj[prop] = value
        if (obj instanceof Module) {
            obj.render();
        }
        // for (const key in obj) {
        //     if (obj[key] instanceof Module) {
        //         obj[key].render();
        //     }
        // }

        return true
    }

    apply(target: any, thisArg: any, argumentsList?: any): any {
        console.log('SimProxyMethodHandler apply', target, 'thisArg', thisArg, 'arg', argumentsList);
        // console.log('SimProxyMethodHandler apply', 'arg', argumentsList);
        return target.apply(thisArg, argumentsList);
        // return argumentsList[0] + argumentsList[1] + argumentsList[2];
    }

    has(target: any, key: PropertyKey): boolean {
        // console.log('isProxy: ', target, key);
        return key in target;
    }
}

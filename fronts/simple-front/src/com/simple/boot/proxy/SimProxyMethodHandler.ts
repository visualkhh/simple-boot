import {Module} from '@src/com/simple/boot/module/Module'
import {simstanceManager} from '@src/com/simple/boot/simstance/SimstanceManager'

export class SimProxyMethodHandler implements ProxyHandler<any> {
    public get(target: any, name: string): any {
        return target[name];
    }

    public set(obj: any, prop: string, value: any): boolean {
        value = simstanceManager.proxy(value, Module);

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
        return target.apply(thisArg, argumentsList);
    }

    has(target: any, key: PropertyKey): boolean {
        if (key === 'isProxy') {
            return true;
        }
        return key in target;
    }
}

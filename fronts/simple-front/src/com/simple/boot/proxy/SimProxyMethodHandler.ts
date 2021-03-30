import {Module} from '../../../../com/simple/boot/module/Module'
import {simstanceManager} from '../../../../com/simple/boot/simstance/SimstanceManager'
import {ConstructorType} from '@src/com/simple/boot/types/Types'

export class SimProxyMethodHandler implements ProxyHandler<any> {
    public get(target: any, name: string): any {
        return target[name]
    }

    public set(obj: any, prop: string, value: any): boolean {
        value = simstanceManager.proxy(value, Module)

        obj[prop] = value
        if ('isProxy' in obj && obj instanceof Module) {
            obj.render()
        } else if (obj instanceof Module) {
            try {
                const sim = simstanceManager.getOrNewSim(obj.constructor as ConstructorType<any>)
                if (sim) {
                    sim.render();
                } else {
                    obj.render();
                }
            } catch (e) {
                obj.render();
            }
        }
        // for (const key in obj) {
        //     if (obj[key] instanceof Module) {
        //         obj[key].render();
        //     }
        // }

        return true
    }

    defineProperty?(target: any, p: PropertyKey, attributes: PropertyDescriptor): boolean {
        return true
    }

    apply(target: any, thisArg: any, argumentsList?: any): any {
        return target.apply(thisArg, argumentsList)
    }

    has(target: any, key: PropertyKey): boolean {
        if (key === 'isProxy') {
            return true
        }
        return key in target
    }
}

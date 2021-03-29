import 'reflect-metadata'
import {ConstructorType} from '../../../../com/simple/boot/types/Types'
import {NoSuchSim} from '../../../../com/simple/boot/throwable/NoSuchSim'
import {SimProxyMethodHandler} from '../../../../com/simple/boot/proxy/SimProxyMethodHandler'
import {Module} from '../../../../com/simple/boot/module/Module'

export class SimstanceManager {
    private _storege = new Map<ConstructorType<any>, any>()

    constructor() {
        this._storege.set(SimstanceManager, this);
    }

    get storege(): Map<ConstructorType<any>, any> {
        return this._storege
    }

    getOrNewSim<T>(k: ConstructorType<T>): T {
        let newVar = this.storege.get(k)
        if (!newVar) {
            newVar = this.resolve(k)
        }
        return newVar
    }

    getOrNewSims<T>(k: ConstructorType<T>): T[] {
        const list = new Array<T>(0);

        // console.log('-storeges--->', this._storege)
        this.storege.forEach((value, key, mapObject) => {
            let sw = false;
            if (value && value instanceof k) {
                sw = true;
                // eslint-disable-next-line no-prototype-builtins
            } else if (key === k || k.isPrototypeOf(key)) {
                sw = true;
            }
            if (sw) {
                if (!value) {
                    value = this.resolve(key);
                }
                list.push(value);
            }
            // console.log('getSims key:', key, ' input:', k, ' value:', value, '--->', key instanceof k, '---->', sw)
        })
        // let newVar = this.storege.get(k)
        // if (!newVar) {
        //     newVar = this.resolve(k)
        // }
        return list;
        // return newVar
    }

    register(target: ConstructorType<any>): void {
        const registed = this._storege.get(target)
        if (!registed) {
            this._storege.set(target, undefined)
        }
    }

    resolve<T>(target: ConstructorType<any>): T {
        const registed = this._storege.get(target)
        if (registed) {
            return registed as T
        }

        if (this._storege.has(target) && undefined === registed) {
            const r = this.newSime(target)
            this._storege.set(target, r)
            return r
        }
        throw new NoSuchSim('no simple instance')
    }

    public newSime<T>(target: ConstructorType<T>): T {
        const paramTypes = Reflect.getMetadata('design:paramtypes', target) || []
        const injections = paramTypes.map((token: ConstructorType<any>) => {
            return this.resolve<any>(token)
        })
        const r = new target(...injections)
        return this.proxy(r, Module);
    }

    public proxy<T>(target: T, type?: ConstructorType<any>): T {
        if ((type ? target instanceof type : true) && (!('isProxy' in target))) {
            for (const key in target) {
                target[key] = this.proxy(target[key], type);
            }
            return new Proxy(target, new SimProxyMethodHandler())
        } else {
            return target;
        }
    }
}
export const simstanceManager = new SimstanceManager();
//
//
// export const SimstanceManager = new class {
//     private _storege = new Map<ConstructorType<any>, any>()
//
//     constructor() {
//         //this._storege.set(SimstanceManager, this);
//     }
//
//     get storege(): Map<ConstructorType<any>, any> {
//         return this._storege
//     }
//
//     getSim<T>(k: ConstructorType<T>): T {
//         let newVar = this.storege.get(k)
//         if (!newVar) {
//             newVar = this.resolve(k)
//         }
//         return newVar
//     }
//
//     register(target: ConstructorType<any>): void {
//         const registed = this._storege.get(target)
//         if (!registed) {
//             this._storege.set(target, undefined)
//         }
//     }
//
//     resolve<T>(target: ConstructorType<any>): T {
//         const registed = this._storege.get(target)
//         if (registed) {
//             return registed as T
//         }
//
//         if (this._storege.has(target) && undefined === registed) {
//             const paramTypes = Reflect.getMetadata('design:paramtypes', target) || []
//             const injections = paramTypes.map((token: ConstructorType<any>) => {
//                 return SimstanceManager.resolve<any>(token)
//             })
//             let r = new target(...injections)
//             for (const key in r) {
//                 if (r[key] instanceof Module) {
//                     r[key] = new Proxy(r[key], new SimProxyMethodHandler())
//                 }
//             }
//
//             if (r instanceof Module) {
//                 r = new Proxy(r, new SimProxyMethodHandler())
//             }
//             this._storege.set(target, r)
//             return r
//         }
//         throw new NoSuchSim('no simple instance')
//     }
// }()

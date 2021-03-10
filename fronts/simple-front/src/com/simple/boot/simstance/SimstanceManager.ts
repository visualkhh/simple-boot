import 'reflect-metadata'
import {ConstructorType} from '@src/com/simple/boot/types/Types'
import {NoSuchSim} from '@src/com/simple/boot/throwable/NoSuchSim'
import {SimProxyMethodHandler} from '@src/com/simple/boot/proxy/SimProxyMethodHandler'
import {Module} from '@src/com/simple/boot/module/Module'
// import {SimProxyMethodHandler} from '@src/com/simple/boot/proxy/SimProxyMethodHandler'

export const SimstanceManager = new class {
    private _storege = new Map<ConstructorType<any>, any>()

    get storege(): Map<ConstructorType<any>, any> {
        return this._storege;
    }

    getSim<T>(k: ConstructorType<T>): T {
        return this.storege.get(k);
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
            const paramTypes = Reflect.getMetadata('design:paramtypes', target) || []
            const injections = paramTypes.map((token: ConstructorType<any>) => {
                return SimstanceManager.resolve<any>(token)
            })
            let r = new target(...injections)
            // proxy는 나중에.좀더 좋은 방법을 찾아보자.

            // const obj = {};
            // Reflect.defineMetadata("key", "value", obj, "name");
            // const result = Reflect.getOwnMetadataKeys(obj, 'name');
            // const result = Reflect.getMetadata('design:properties', r) || []
            // console.log('proxy ', r, r instanceof Module)

            for (const key in r) {
                // if (typeof r[key] === 'object' && key === 'admins') {
                if (r[key] instanceof Module) {
                    r[key] = new Proxy(r[key], new SimProxyMethodHandler())
                }
            }

            if (r instanceof Module) {
                r = new Proxy(r, new SimProxyMethodHandler());
            }
            // r.subPu
            // r = new Proxy(target, new SimProxyMethodHandler());
            console.log('simstanceManager set ', target, r, r instanceof Module)
            // console.log('simstanceManager set proxy', r.isProxy())
            this._storege.set(target, r)
            return r
        }
        throw new NoSuchSim('no simple instance');
    }
}()

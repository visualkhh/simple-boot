import 'reflect-metadata'
import {ConstructorType} from '@src/com/simple/boot/types/Types'
import {NoSuchSim} from '@src/com/simple/boot/throwable/NoSuchSim'
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
            const r = new target(...injections)
            // proxy는 나중에.좀더 좋은 방법을 찾아보자.
            // for (const key in r) {
            //     r[key] = new Proxy(r[key], new SimProxyMethodHandler());
            // }
            // r.subPu
            // r = new Proxy(target, new SimProxyMethodHandler());
            this._storege.set(target, r)
            return r
        }
        throw new NoSuchSim('no simple instance');
    }
}()

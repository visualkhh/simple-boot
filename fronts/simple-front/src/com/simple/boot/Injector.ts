import "reflect-metadata";
import {ConstructorType} from "@src/com/simple/boot/types/Types";


export const Injector = new class {
    private storeges = new Map<ConstructorType<any>, any>();
    resolve<T>(target: ConstructorType<any>): T {
        let old = this.storeges.get(target);
        if(old) {
            return old as T;
        }

        let tokens = Reflect.getMetadata('design:paramtypes', target) || [],
            injections = tokens.map((token: ConstructorType<any>) => Injector.resolve<any>(token));
        let r = new target(...injections);
        this.storeges.set(target, r);
        return r;
    }
}

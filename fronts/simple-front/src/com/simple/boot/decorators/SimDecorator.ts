import {ConstructorType, GenericClassDecorator} from '../types/Types';
import {SimstanceManager} from '@src/com/simple/boot/simstance/SimstanceManager'

export const Sim = (): GenericClassDecorator<ConstructorType<any>> => {
    return (target: ConstructorType<any>) => {
        // console.log('sim register ->', target)
        SimstanceManager.register(target);
    }
}

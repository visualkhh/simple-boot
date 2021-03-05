import {GenericClassDecorator, ConstructorType} from "../types/Types";

/**
 * @returns {GenericClassDecorator<ConstructorType<any>>}
 * @constructor
 */
export const Service = () : GenericClassDecorator<ConstructorType<any>> => {
  return (target: ConstructorType<any>) => {
    // do something with `target`, e.g. some kind of validation or passing it to the Injector and store them
  };
};

import {Module} from '../../../src/com/simple/boot/module/Module'

export class LoopModule extends Module {
    public datas = [1, 2, 3, 4, 5, 6, 7]

    constructor(selector: string) {
        super(selector,
            `<ul>
                {{#each datas as |data i|}}
                    <li>{{data}}</li>
                {{/each}}
            </ul>`)
    }
}

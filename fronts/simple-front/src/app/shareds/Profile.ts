import {Module} from '../../../src/com/simple/boot/module/Module'
// import {Sim} from '@src/com/simple/boot/decorators/SimDecorator'
import {User} from '@src/app/models/UserResponse'
import html from './profile.html'

export class Profile extends Module {
    template= html;
    private data: User | undefined;

    constructor() {
        super('profile')
    }

    public setUser(data: User) {
        this.data = data;
    }
}

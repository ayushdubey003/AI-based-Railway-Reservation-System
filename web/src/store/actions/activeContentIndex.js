import {ACTIVE_CONTENT_INDEX} from "../actionTypes";

export default function activeContentIndex(index){
    // debugger;
    return {
        type: ACTIVE_CONTENT_INDEX,
        index: index
    }
}
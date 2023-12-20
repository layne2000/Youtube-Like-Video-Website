package org.example;

import org.example.constant.UserConstant;
import org.example.entity.FollowingGroup;
import org.example.entity.UserFollowing;
import org.example.entity.UserInfo;
import org.example.exception.CustomizedException;
import org.example.mapper.FollowingGroupMapper;
import org.example.mapper.UserFollowingMapper;
import org.example.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserFollowingService {
    private final UserFollowingMapper userFollowingMapper;
    private final FollowingGroupMapper followingGroupMapper;
    private final UserInfoMapper userInfoMapper;
    private final UserService userService;

    @Autowired
    public UserFollowingService(UserFollowingMapper userFollowingMapper, FollowingGroupMapper
            followingGroupMapper, UserInfoMapper userInfoMapper, UserService userService) {
        this.userFollowingMapper = userFollowingMapper;
        this.followingGroupMapper = followingGroupMapper;
        this.userInfoMapper = userInfoMapper;
        this.userService = userService;
    }

    @Transactional
    public void addUserFollowing(UserFollowing userFollowing) {
        Long followingId = userFollowing.getFollowingId();
        Long userId = userFollowing.getUserId();
        if (followingId.equals(userId)) {
            throw new CustomizedException("User can't follow themselves");
        }
        if (userService.getUserById(followingId) == null) {
            throw new CustomizedException("The user whose id is followingId doesn't exist");
        }
        if (userFollowing.getGroupId() != null) {
            if (followingGroupMapper.getFollowingGroupById(userFollowing.getGroupId()) == null) {
                throw new CustomizedException("The group whose id is groupId doesn't exist");
            }
        } else {
            // TODO: when do we add default group
            FollowingGroup followingGroup = followingGroupMapper.getFollowingGroupByTypeAndUseId(UserConstant.FOLLOWING_GROUP_DEFAULT_TYPE, userFollowing.getUserId());
            userFollowing.setGroupId(followingGroup.getId());
        }
        //avoid adding a duplicate userFollowing record
        userFollowingMapper.deleteUserFollowing(userId, followingId);
        userFollowing.setCreatedTime(LocalDateTime.now());
        userFollowingMapper.addUserFollowing(userFollowing);
    }

    public List<FollowingGroup> getUserFollowings(Long userId) {
        List<UserFollowing> userFollowingList = userFollowingMapper.getUserFollowingList(userId);
        List<UserInfo> userInfoList = new ArrayList<>();
        for (UserFollowing userFollowing : userFollowingList) {
            userInfoList.add(userInfoMapper.getUserInfoByUserId(userFollowing.getUserId()));
        }

        List<FollowingGroup> ret = new ArrayList<>();
        FollowingGroup allFollowings = new FollowingGroup();
        allFollowings.setUserId(userId);
        allFollowings.setCreatedTime(LocalDateTime.now());
        allFollowings.setName("AllFollowings");
        allFollowings.setFollowingUserInfoList(userInfoList);
        ret.add(allFollowings);

        // key is groupId, value is the userInfo list
        Map<Long, List<UserInfo>> hashMap = new HashMap<>();
        for (int i = 0; i < userFollowingList.size(); ++i) {
            Long groupId = userFollowingList.get(i).getGroupId();
            UserInfo userInfo = userInfoList.get(i);
            if (hashMap.containsKey(groupId)) {
                hashMap.get(groupId).add(userInfo);
            } else {
                List<UserInfo> tempList = new ArrayList<>();
                tempList.add(userInfo);
                hashMap.put(groupId, tempList);
            }
        }
        List<FollowingGroup> followingGroupList = followingGroupMapper.getFollowingGroupListByUserId(userId);
        for (FollowingGroup followingGroup : followingGroupList) {
            followingGroup.setFollowingUserInfoList(hashMap.get(followingGroup.getId()));
            ret.add(followingGroup);
        }
        return ret;
    }

    public Long addUserFollowingGroup(FollowingGroup followingGroup) {
        followingGroup.setType("3"); // customized
        followingGroup.setCreatedTime(LocalDateTime.now());
        followingGroupMapper.addFollowingGroup(followingGroup);
        return followingGroup.getId();
    }

    public List<FollowingGroup> getUserFollowingGroupList(Long userId) {
        return followingGroupMapper.getFollowingGroupListByUserId(userId);
    }

    // get all fans of the current user (add fan userInfo (and set if it's followed back by the
    // current user) to each userFollowing
    public List<UserFollowing> getUserFans(Long userId) {
        List<UserFollowing> fansList = userFollowingMapper.getUserFans(userId);
        List<UserFollowing> userFollowingList = userFollowingMapper.getUserFollowingList(userId);
        Set<Long> userFollowingSet = new HashSet<>();
        for (UserFollowing userFollowing : userFollowingList) {
            userFollowingSet.add(userFollowing.getFollowingId());
        }
        for (UserFollowing userFollowing : fansList) {
            Long fanId = userFollowing.getUserId();
            userFollowing.setUserInfo(userInfoMapper.getUserInfoByUserId(fanId));
            userFollowing.getUserInfo().setFollowed(false);
            if (userFollowingSet.contains(fanId)) {
                userFollowing.getUserInfo().setFollowed(true);
            }
        }
        return fansList;
    }

    public List<UserInfo> addUserInfoFollowedStatus(List<UserInfo> userInfoList, Long userId) {
        List<UserFollowing> userFollowingList = userFollowingMapper.getUserFollowingList(userId);
        Set<Long> followingIdSet = new HashSet<>();
        for (UserFollowing userFollowing : userFollowingList) {
            followingIdSet.add(userFollowing.getFollowingId());
        }
        for (UserInfo userInfo : userInfoList) {
            userInfo.setFollowed(false);
            if (followingIdSet.contains(userInfo.getUserId())) {
                userInfo.setFollowed(true);
            }
        }
        return userInfoList;
    }
}

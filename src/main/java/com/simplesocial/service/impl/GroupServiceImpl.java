package com.simplesocial.service.impl;

import com.simplesocial.entity.Group;
import com.simplesocial.entity.User;
import com.simplesocial.exception.ResourceNotFoundException;
import com.simplesocial.repository.GroupRepository;
import com.simplesocial.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;

    @Override
    @Transactional
    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public Group findById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + id));
    }

    @Override
    @Transactional
    public Group updateGroup(Long id, Group groupDetails) {
        Group group = findById(id);

        if (groupDetails.getName() != null) {
            group.setName(groupDetails.getName());
        }
        if (groupDetails.getDescription() != null) {
            group.setDescription(groupDetails.getDescription());
        }
        if (groupDetails.getCoverImage() != null) {
            group.setCoverImage(groupDetails.getCoverImage());
        }
        if (groupDetails.getIsPublic() != group.getIsPublic()) {
            group.setIsPublic(groupDetails.getIsPublic());
        }

        return groupRepository.save(group);
    }

    @Override
    @Transactional
    public void deleteGroup(Long id) {
        Group group = findById(id);
        groupRepository.delete(group);
    }

    @Override
    public Page<Group> findByCreator(User creator, Pageable pageable) {
        return groupRepository.findByCreator(creator, pageable);
    }

    @Override
    public Page<Group> searchPublicGroups(String query, Pageable pageable) {
        return groupRepository.searchPublicGroups(query, pageable);
    }

    @Override
    public Page<Group> findUserGroups(User user, Pageable pageable) {
        return groupRepository.findUserGroups(user, pageable);
    }
}